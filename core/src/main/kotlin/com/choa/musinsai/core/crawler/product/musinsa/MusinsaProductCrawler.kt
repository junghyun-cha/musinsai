package com.choa.musinsai.core.crawler.product.musinsa

import com.choa.musinsai.core.crawler.product.ProductCrawler
import com.choa.musinsai.core.crawler.product.ProductSearchRequest
import com.choa.musinsai.core.crawler.product.ProductSearchResponse
import com.choa.musinsai.core.domain.Gender
import com.choa.musinsai.core.domain.Product
import com.choa.musinsai.core.domain.ShoppingPlatform
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class MusinsaProductCrawler(private val webClientBuilder: WebClient.Builder) : ProductCrawler {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val BASE_URL = "https://api.musinsa.com/api2/dp/v1/plp/goods"
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        private const val TIMEOUT_SECONDS = 30L
    }

    private val webClient = webClientBuilder
        .baseUrl(BASE_URL)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .build()

    override fun search(request: ProductSearchRequest): ProductSearchResponse {
        return try {
            logger.info { "무신사 검색 시작: keyword=${request.keyword}, page=${request.page}" }

            val response = searchMusinsaApi(request)
            val products = response.data.list.map { convertToProduct(it) }

            logger.info {
                "무신사 검색 완료: 총 ${response.data.pagination.totalCount}개 중 ${products.size}개 상품 조회"
            }

            ProductSearchResponse(
                products = products,
                totalCount = response.data.pagination.totalCount,
                categoryName = null
            )
        } catch (e: WebClientResponseException) {
            logger.error(e) { "무신사 API 호출 실패: status=${e.statusCode}, body=${e.responseBodyAsString}" }
            ProductSearchResponse(emptyList(), 0, null)
        } catch (e: Exception) {
            logger.error(e) { "무신사 검색 중 오류 발생" }
            ProductSearchResponse(emptyList(), 0, null)
        }
    }

    private fun searchMusinsaApi(request: ProductSearchRequest): MusinsaApiResponse {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .queryParam("keyword", request.keyword)
                    .queryParam("gf", request.gender)
                    .queryParam("sortCode", request.sortCode)
                    .queryParam("page", request.page)
                    .queryParam("size", request.size)
                    .queryParam("caller", "SEARCH")
                    .build()
            }
            .retrieve()
            .bodyToMono(MusinsaApiResponse::class.java)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .onErrorResume { error ->
                logger.error(error) { "API 호출 중 오류 발생" }
                Mono.just(MusinsaApiResponse())
            }
            .block() ?: MusinsaApiResponse()
    }

    private fun convertToProduct(musinsaProduct: MusinsaProduct): Product {
        return Product(
            id = musinsaProduct.goodsNo.toString(),
            name = musinsaProduct.goodsName,
            brandName = musinsaProduct.brandName,
            imageUrl = musinsaProduct.goodsImageUrl.orEmpty(),
            productUrl = musinsaProduct.goodsLinkUrl.orEmpty(),
            originalPrice = musinsaProduct.normalPrice.toBigDecimal(),
            salePrice = musinsaProduct.price.toBigDecimal(),
            discountRate = musinsaProduct.saleRate,
            gender = parseGender(musinsaProduct.displayGenderText),
            isSoldOut = musinsaProduct.isSoldOut,
            reviewCount = musinsaProduct.reviewCount,
            reviewScore = musinsaProduct.reviewScore,
            tags = musinsaProduct.tags,
            platform = ShoppingPlatform.MUSINSA
        )
    }

    private fun parseGender(genderText: String?): Gender {
        return when (genderText?.trim()?.lowercase()) {
            "남성", "male", "m" -> Gender.MALE
            "여성", "female", "f", "w" -> Gender.FEMALE
            "공용", "unisex", "u" -> Gender.UNISEX
            "키즈", "kids", "k" -> Gender.KIDS
            else -> Gender.UNISEX
        }
    }
}
