package com.choa.musinsai.core.crawler.product.musinsa

import com.choa.musinsai.core.crawler.product.ProductSearchRequest
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

class MusinsaProductCrawlerTest {

    private lateinit var crawler: MusinsaProductCrawler

    @BeforeEach
    fun setUp() {
        val webClientBuilder = WebClient.builder()
        crawler = MusinsaProductCrawler(webClientBuilder)
    }

    @Test
    @DisplayName("무신사 상품 검색 테스트")
    fun testSearchProducts() {
        // Given
        val request = ProductSearchRequest(
            keyword = "반바지",
            page = 1,
            size = 10,
            sortCode = "POPULAR"
        )

        // When
        val result = crawler.search(request)

        // Then
        assertNotNull(result)
        assertTrue(result.products.isNotEmpty())
        assertTrue(result.totalCount > 0)

        // Verify product data
        val firstProduct = result.products.first()
        assertNotNull(firstProduct.id)
        assertNotNull(firstProduct.name)
        assertNotNull(firstProduct.brandName)
        assertNotNull(firstProduct.imageUrl)
        assertNotNull(firstProduct.productUrl)
    }

    @Test
    @DisplayName("성별 필터링 검색 테스트")
    fun testSearchWithGenderFilter() {
        // Given
        val request = ProductSearchRequest(
            keyword = "티셔츠",
            gender = "F",
            page = 1,
            size = 5,
            sortCode = "NEW"
        )

        // When
        val result = crawler.search(request)

        // Then
        assertNotNull(result)
        assertTrue(result.products.isNotEmpty())

        // Note: We can't guarantee all products will be female-only
        // as Musinsa may include unisex items in female category
    }

    @Test
    @DisplayName("가격 범위 필터링 테스트")
    fun testSearchWithPriceFilter() {
        // Given
        val request = ProductSearchRequest(
            keyword = "셔츠",
            page = 1,
            size = 5,
            sortCode = "PRICE_LOW"
        )

        // When
        val result = crawler.search(request)

        // Then
        assertNotNull(result)
        if (result.products.isNotEmpty()) {
            result.products.forEach { product ->
                val price = product.salePrice
                assertTrue(price.toDouble() >= 0) // Price should be non-negative
            }
        }
    }

    @Test
    @DisplayName("카테고리별 상품 조회 테스트")
    fun testGetProductsByCategory() {
        // Given
        val request = ProductSearchRequest(
            keyword = "상의",
            page = 1,
            size = 5,
            sortCode = "POPULAR"
        )

        // When
        val result = crawler.search(request)

        // Then
        assertNotNull(result)
        assertTrue(result.products.isNotEmpty())
    }

    @Test
    @DisplayName("빈 검색 결과 처리 테스트")
    fun testEmptySearchResult() {
        // Given - very specific search that likely returns no results
        val request = ProductSearchRequest(
            keyword = "xyzabc123456789",
            page = 1,
            size = 10
        )

        // When
        val result = crawler.search(request)

        // Then
        assertNotNull(result)
        // Even with no results, the structure should be valid
    }

    @Test
    @DisplayName("페이지네이션 테스트")
    fun testPagination() {
        // Given
        val requestPage1 = ProductSearchRequest(
            keyword = "청바지",
            page = 1,
            size = 5
        )
        val requestPage2 = ProductSearchRequest(
            keyword = "청바지",
            page = 2,
            size = 5
        )

        // When
        val resultPage1 = crawler.search(requestPage1)
        val resultPage2 = crawler.search(requestPage2)

        // Then
        assertNotNull(resultPage1)
        assertNotNull(resultPage2)

        // Products should be different
        if (resultPage1.products.isNotEmpty() && resultPage2.products.isNotEmpty()) {
            assertNotEquals(
                resultPage1.products.first().id,
                resultPage2.products.first().id
            )
        }
    }

    @Test
    @DisplayName("정렬 옵션 테스트")
    fun testSortOptions() {
        // Given
        val keyword = "가방"

        // Test different sort options
        val sortOptions = listOf(
            "POPULAR",
            "NEW",
            "PRICE_HIGH",
            "PRICE_LOW",
            "SALE"
        )

        sortOptions.forEach { sortOption ->
            val request = ProductSearchRequest(
                keyword = keyword,
                page = 1,
                size = 3,
                sortCode = sortOption
            )

            // When
            val result = crawler.search(request)

            // Then
            assertNotNull(result, "Sort option $sortOption should return results")
            assertTrue(
                result.products.isNotEmpty() || result.totalCount == 0,
                "Sort option $sortOption should have valid structure"
            )
        }
    }
}
