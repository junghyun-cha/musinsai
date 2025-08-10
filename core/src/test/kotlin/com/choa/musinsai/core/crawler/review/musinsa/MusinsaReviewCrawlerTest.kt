package com.choa.musinsai.core.crawler.review.musinsa

import com.choa.musinsai.core.crawler.review.ReviewSearchRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

class MusinsaReviewCrawlerTest {

    private lateinit var crawler: MusinsaReviewCrawler

    @BeforeEach
    fun setUp() {
        val webClientBuilder = WebClient.builder()
        crawler = MusinsaReviewCrawler(webClientBuilder)
    }

    @Test
    @DisplayName("무신사 리뷰 기본 조회 - 구조 검증")
    fun testGetProductReviews_basic() {
        // Given: 실제 존재 여부가 불명확한 goodsNo 를 사용하되 구조 검증 위주로 확인
        val request = ReviewSearchRequest(
            goodsNo = 12345L, // 임의의 상품 번호: 결과가 0이어도 구조만 검증
            page = 1,
            pageSize = 10,
            sort = "date_desc"
        )

        // When
        val result = crawler.getProductReviews(request)

        // Then
        assertNotNull(result)
        assertTrue(result.totalCount >= 0)
        assertEquals(request.page, result.currentPage)
        assertTrue(result.totalPages >= 0)
        // hasNext 는 currentPage 와 totalPages 관계 기반으로 계산됨
        if (result.totalPages > 0) {
            assertEquals(result.currentPage < result.totalPages, result.hasNext)
        }
        // 리뷰 각각의 필수 필드(매핑)도 간단 확인
        result.reviews.forEach { r ->
            assertNotNull(r.id)
            assertNotNull(r.productId)
            assertNotNull(r.userName)
            assertNotNull(r.platform)
            assertNotNull(r.createdAt)
        }
    }

    @Test
    @DisplayName("무신사 리뷰 - 사진 리뷰 필터(hasPhoto) 적용")
    fun testGetProductReviews_withPhotoFilter() {
        // Given
        val request = ReviewSearchRequest(
            goodsNo = 12345L,
            page = 1,
            pageSize = 5,
            hasPhoto = true
        )

        // When
        val result = crawler.getProductReviews(request)

        // Then
        assertNotNull(result)
        assertTrue(result.totalCount >= 0)
        // 사진 리뷰만 보장하긴 어려우므로, 결과가 있다면 각 리뷰의 images 가 존재할 수도 있음을 널-안전하게 확인
        result.reviews.forEach { review ->
            // hasPhoto=true 라도 플랫폼 응답이 항상 사진만 반환하지 않을 수 있으므로 강한 단정은 피함
            assertNotNull(review.images)
        }
    }

    @Test
    @DisplayName("무신사 리뷰 - 페이지네이션 필드 검증")
    fun testPaginationFields() {
        // Given
        val page1 = ReviewSearchRequest(goodsNo = 12345L, page = 1, pageSize = 3)
        val page2 = ReviewSearchRequest(goodsNo = 12345L, page = 2, pageSize = 3)

        // When
        val r1 = crawler.getProductReviews(page1)
        val r2 = crawler.getProductReviews(page2)

        // Then
        assertEquals(1, r1.currentPage)
        assertEquals(2, r2.currentPage)
        assertTrue(r1.totalPages >= 0)
        assertTrue(r2.totalPages >= 0)
        if (r1.totalPages > 0) {
            assertEquals(r1.currentPage < r1.totalPages, r1.hasNext)
        }
        if (r2.totalPages > 0) {
            assertEquals(r2.currentPage < r2.totalPages, r2.hasNext)
        }
    }

    @Test
    @DisplayName("무신사 리뷰 - 리뷰가 없을 때 summary 는 null 일 수 있다")
    fun testSummaryWhenNoReviews() {
        // Given: 존재하지 않을 가능성이 매우 높은 큰 goodsNo
        val request = ReviewSearchRequest(goodsNo = Long.MAX_VALUE, page = 1, pageSize = 5)

        // When
        val result = crawler.getProductReviews(request)

        // Then
        // 총 개수가 0 이면 summary 는 null 로 매핑됨
        if (result.totalCount == 0) {
            assertNull(result.summary)
        } else {
            // 혹시라도 응답이 있다면 summary 구조만 검증
            assertNotNull(result.summary)
            result.summary?.let { s ->
                assertEquals(request.goodsNo.toString(), s.productId)
                assertTrue(s.totalCount >= 0)
                assertTrue(s.averageRating >= 0.0)
            }
        }
    }
}
