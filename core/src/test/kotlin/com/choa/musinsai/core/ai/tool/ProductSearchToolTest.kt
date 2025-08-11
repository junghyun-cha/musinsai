package com.choa.musinsai.core.ai.tool

import com.choa.musinsai.core.provider.crawler.product.Product
import com.choa.musinsai.core.provider.crawler.product.ProductCrawler
import com.choa.musinsai.core.provider.crawler.product.ProductSearchRequest
import com.choa.musinsai.core.provider.crawler.product.ProductSearchResponse
import com.choa.musinsai.core.provider.crawler.Gender
import com.choa.musinsai.core.provider.crawler.ShoppingPlatform
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import java.math.BigDecimal

class ProductSearchToolTest {

    private lateinit var productCrawler: ProductCrawler
    private lateinit var productSearchTool: ProductSearchTool

    @BeforeEach
    fun setUp() {
        productCrawler = mockk()
        productSearchTool = ProductSearchTool(productCrawler)
    }

    @Test
    @DisplayName("상품 검색 도구가 정상적으로 동작한다")
    fun testProductSearchTool() {
        // Given
        val request = ProductSearchTool.Request(
            keyword = "셔츠",
            gender = "M",
            sortCode = "POPULAR",
            page = 1,
            size = 20
        )

        val mockProducts = listOf(
            Product(
                id = "1",
                name = "오버핏 셔츠",
                brand = "브랜드A",
                price = 49000,
                originalPrice = 59000,
                discountRate = 17,
                url = "https://musinsa.com/product/1",
                imageUrl = "https://image.musinsa.com/1.jpg",
                rating = 4.5,
                reviewCount = 100
            ),
            Product(
                id = "2",
                name = "슬림핏 셔츠",
                brand = "브랜드B",
                price = 39000,
                originalPrice = 45000,
                discountRate = 13,
                url = "https://musinsa.com/product/2",
                imageUrl = "https://image.musinsa.com/2.jpg",
                rating = 4.3,
                reviewCount = 85
            )
        )

        val mockResponse = ProductSearchResponse(
            products = mockProducts,
            totalCount = 100,
            currentPage = 1,
            totalPages = 5
        )

        every { 
            productCrawler.search(any()) 
        } returns mockResponse

        // When
        val response = productSearchTool.apply(request)

        // Then
        assertTrue(response.success)
        assertEquals(2, response.products.size)
        assertEquals(100, response.totalCount)
        assertEquals(1, response.currentPage)
        assertEquals(5, response.totalPages)
        assertNull(response.errorMessage)

        val firstProduct = response.products.first()
        assertEquals("1", firstProduct.id)
        assertEquals("오버핏 셔츠", firstProduct.name)
        assertEquals("브랜드A", firstProduct.brand)
        assertEquals(49000, firstProduct.price)
        assertEquals(17, firstProduct.discountRate)

        verify(exactly = 1) {
            productCrawler.search(
                match {
                    it.keyword == "셔츠" &&
                    it.gender == "M" &&
                    it.sortCode == "POPULAR" &&
                    it.page == 1 &&
                    it.size == 20
                }
            )
        }
    }

    @Test
    @DisplayName("검색 중 오류 발생 시 에러 응답을 반환한다")
    fun testProductSearchToolError() {
        // Given
        val request = ProductSearchTool.Request(
            keyword = "셔츠"
        )

        every { 
            productCrawler.search(any()) 
        } throws RuntimeException("크롤링 실패")

        // When
        val response = productSearchTool.apply(request)

        // Then
        assertFalse(response.success)
        assertEquals(0, response.products.size)
        assertEquals(0, response.totalCount)
        assertEquals("크롤링 실패", response.errorMessage)
    }

    @Test
    @DisplayName("기본값이 올바르게 설정된다")
    fun testDefaultValues() {
        // Given
        val request = ProductSearchTool.Request(keyword = "바지")

        val mockResponse = ProductSearchResponse(
            products = emptyList(),
            totalCount = 0,
            currentPage = 1,
            totalPages = 0
        )

        every { 
            productCrawler.search(any()) 
        } returns mockResponse

        // When
        productSearchTool.apply(request)

        // Then
        verify(exactly = 1) {
            productCrawler.search(
                match {
                    it.keyword == "바지" &&
                    it.gender == "A" &&  // 기본값
                    it.sortCode == "POPULAR" &&  // 기본값
                    it.page == 1 &&  // 기본값
                    it.size == 20  // 기본값
                }
            )
        }
    }
}
