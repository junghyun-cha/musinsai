package com.choa.musinsai.core.crawler.review.musinsa

import com.choa.musinsai.core.crawler.review.ReviewCrawler
import com.choa.musinsai.core.crawler.review.ReviewSearchRequest
import com.choa.musinsai.core.domain.Gender
import com.choa.musinsai.core.domain.Review
import com.choa.musinsai.core.domain.ReviewSearchResult
import com.choa.musinsai.core.domain.ReviewSummary
import com.choa.musinsai.core.domain.ShoppingPlatform
import com.choa.musinsai.core.domain.UserProfile
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class MusinsaReviewCrawler(private val webClientBuilder: WebClient.Builder) : ReviewCrawler {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val BASE_URL = "https://goods.musinsa.com/api2/review/v1/view/list"
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        private const val TIMEOUT_SECONDS = 30L
    }

    private val webClient = webClientBuilder
        .baseUrl(BASE_URL)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .build()

    override fun getProductReviews(request: ReviewSearchRequest): ReviewSearchResult {
        return try {
            logger.info { "무신사 리뷰 조회 시작: goodsNo=${request.goodsNo}, page=${request.page}" }

            val response = callApi(request)
            val reviews = response.data.list.map { toDomainReview(it) }
            val pagination = response.data.page
            val currentPage = pagination?.page ?: request.page
            val totalPages = pagination?.totalPages
                ?: if (response.data.total > 0 && request.pageSize > 0) ((response.data.total + request.pageSize - 1) / request.pageSize) else 0
            val hasNext = totalPages > 0 && currentPage < totalPages

            ReviewSearchResult(
                reviews = reviews,
                totalCount = response.data.total,
                currentPage = currentPage,
                totalPages = totalPages,
                hasNext = hasNext,
                summary = buildSummary(request.goodsNo, response)
            )
        } catch (e: Exception) {
            logger.error(e) { "무신사 리뷰 조회 중 오류 발생" }
            ReviewSearchResult(
                reviews = emptyList(),
                totalCount = 0,
                currentPage = request.page,
                totalPages = 0,
                hasNext = false,
                summary = null
            )
        }
    }

    private fun callApi(request: ReviewSearchRequest): MusinsaReviewApiResponse {
        return webClient.get()
            .uri { uriBuilder ->
                var builder = uriBuilder
                    .queryParam("goodsNo", request.goodsNo)
                    .queryParam("page", request.page)
                    .queryParam("pageSize", request.pageSize)
                    .queryParam("sort", request.sort)
                request.selectedSimilarNo?.let { builder = builder.queryParam("selectedSimilarNo", it) }
                request.myFilter?.let { builder = builder.queryParam("myFilter", it) }
                request.hasPhoto?.let { builder = builder.queryParam("hasPhoto", it) }
                request.isExperience?.let { builder = builder.queryParam("isExperience", it) }
                builder.build()
            }
            .retrieve()
            .bodyToMono(MusinsaReviewApiResponse::class.java)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .onErrorResume { error ->
                logger.error(error) { "리뷰 API 호출 오류" }
                Mono.just(MusinsaReviewApiResponse())
            }
            .block() ?: MusinsaReviewApiResponse()
    }

    private fun toDomainReview(r: MusinsaReview): Review {
        val images = r.images?.mapNotNull { it.imageUrl } ?: emptyList()
        val createdAt = parseDateTime(r.createDate)
        val profile = UserProfile(
            height = r.userProfileInfo.userHeight,
            weight = r.userProfileInfo.userWeight,
            bodyType = null,
            skinType = r.userProfileInfo.skinType,
            age = null,
            gender = parseGender(r.userProfileInfo.reviewSex)
        )
        return Review(
            id = r.no.toString(),
            productId = r.goods.goodsNo.toString(),
            userId = r.userId,
            userName = r.userProfileInfo.userNickName,
            rating = r.grade.toIntOrNull() ?: 0,
            content = r.content,
            images = images,
            likeCount = r.likeCount,
            commentCount = r.commentCount + r.commentReplyCount,
            purchaseOption = r.goodsOption,
            userProfile = profile,
            createdAt = createdAt,
            isVerifiedPurchase = r.orderOptionNo != 0L,
            platform = ShoppingPlatform.MUSINSA
        )
    }

    private fun buildSummary(goodsNo: Long, res: MusinsaReviewApiResponse): ReviewSummary? {
        if (res.data.total == 0) return null
        val grades = res.data.list.mapNotNull { it.grade.toIntOrNull() }
        val avg = if (grades.isNotEmpty()) grades.average() else 0.0
        val distribution = (1..5).associateWith { g -> grades.count { it == g } }
        return ReviewSummary(
            productId = goodsNo.toString(),
            totalCount = res.data.total,
            averageRating = avg,
            ratingDistribution = distribution,
            positiveKeywords = emptyList(),
            negativeKeywords = emptyList(),
            mostHelpfulReviews = emptyList()
        )
    }

    private fun parseDateTime(dateString: String): LocalDateTime {
        // The API returns format like "2024-06-18 13:22:11" or ISO. Try multiple.
        val candidates = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy.MM.dd")
        )
        for (fmt in candidates) {
            try {
                return when (fmt) {
                    DateTimeFormatter.ofPattern("yyyy.MM.dd") -> LocalDate.parse(dateString, fmt).atStartOfDay()
                    else -> LocalDateTime.parse(dateString, fmt)
                }
            } catch (_: Exception) {
            }
        }
        return LocalDateTime.now()
    }

    private fun parseGender(sex: String?): Gender? {
        return when (sex?.uppercase()) {
            "M", "MALE" -> Gender.MALE
            "F", "FEMALE" -> Gender.FEMALE
            else -> null
        }
    }
}
