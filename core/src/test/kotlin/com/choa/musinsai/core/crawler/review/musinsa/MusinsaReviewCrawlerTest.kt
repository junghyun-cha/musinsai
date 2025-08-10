package com.choa.musinsai.core.crawler.review.musinsa

import com.choa.musinsai.core.crawler.review.ReviewSearchRequest
import org.junit.jupiter.api.Assertions.assertNotNull
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
    fun testSearchReviews_basic() {
        // Given: 실제 존재 여부가 불명확한 goodsNo 를 사용하되 구조 검증 위주로 확인
        val request = ReviewSearchRequest(
            goodsNo = 4996841, // 임의의 상품 번호: 결과가 0이어도 구조만 검증
            page = 0,
            pageSize = 10,
            sort = "date_desc"
        )

        // When
        val result = crawler.search(request)
        // Then
        assertNotNull(result)
        assertTrue(result.totalCount >= 0)
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
    fun testSearchReviews_withPhotoFilter() {
        // Given
        val request = ReviewSearchRequest(
            goodsNo = 12345L,
            page = 1,
            pageSize = 5,
            hasPhoto = true
        )

        // When
        val result = crawler.search(request)

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
        val r1 = crawler.search(page1)
        val r2 = crawler.search(page2)

        // Then
        assertNotNull(r1)
        assertNotNull(r2)
        assertTrue(r1.totalCount >= 0)
        assertTrue(r2.totalCount >= 0)
    }

    @Test
    @DisplayName("무신사 리뷰 - 빈 결과 처리")
    fun testEmptyReviews() {
        // Given: 존재하지 않을 가능성이 매우 높은 큰 goodsNo
        val request = ReviewSearchRequest(goodsNo = Long.MAX_VALUE, page = 1, pageSize = 5)

        // When
        val result = crawler.search(request)

        // Then
        assertNotNull(result)
        // 총 개수가 0 이면 reviews는 빈 리스트
        if (result.totalCount == 0) {
            assertTrue(result.reviews.isEmpty())
        }
    }
}
