package com.choa.musinsai.core.ai.tool

import com.choa.musinsai.core.provider.crawler.product.ProductCrawler
import com.choa.musinsai.core.provider.crawler.product.ProductSearchRequest
import org.springframework.stereotype.Component
import org.springframework.ai.tool.annotation.Tool

@Component
class ProductSearchTool(
    private val productCrawler: ProductCrawler
) {

    /**
     * Spring AI Tool for product searching.
     * The LLM can call this tool to search products by keyword and filters.
     */
    @Tool(description = "상품을 키워드와 필터로 검색합니다. 파라미터: keyword(필수), gender(A/M/F), sortCode(POPULAR/SALE/PRICE_LOW/PRICE_HIGH/NEW), page(1부터), size(페이지 크기)")
    fun apply(request: Request): Response {
        return try {
            val mapped = ProductSearchRequest(
                keyword = request.keyword,
                gender = request.gender ?: DEFAULT_GENDER,
                sortCode = request.sortCode ?: DEFAULT_SORT,
                page = request.page ?: DEFAULT_PAGE,
                size = request.size ?: DEFAULT_SIZE,
                cookieString = request.cookieString
            )

            val result = productCrawler.search(mapped)

            Response(
                success = true,
                products = result.products.map { p ->
                    Response.Product(
                        id = p.id,
                        name = p.name,
                        brand = p.brandName,
                        price = p.salePrice.intValueExact(),
                        originalPrice = p.originalPrice.intValueExact(),
                        discountRate = p.discountRate,
                        url = p.productUrl,
                        imageUrl = p.imageUrl,
                        rating = p.reviewScore,
                        reviewCount = p.reviewCount
                    )
                },
                totalCount = result.totalCount,
                currentPage = result.currentPage,
                totalPages = result.totalPages,
                errorMessage = null
            )
        } catch (e: Exception) {
            Response(
                success = false,
                products = emptyList(),
                totalCount = 0,
                currentPage = 0,
                totalPages = 0,
                errorMessage = e.message
            )
        }
    }

    data class Request(
        val keyword: String,
        val gender: String? = DEFAULT_GENDER, // A: 전체, M: 남성, F: 여성
        val sortCode: String? = DEFAULT_SORT, // POPULAR, SALE, PRICE_LOW, PRICE_HIGH, NEW
        val page: Int? = DEFAULT_PAGE,
        val size: Int? = DEFAULT_SIZE,
        val cookieString: String? = null
    )

    data class Response(
        val success: Boolean,
        val products: List<Product>,
        val totalCount: Int,
        val currentPage: Int,
        val totalPages: Int,
        val errorMessage: String?
    ) {
        data class Product(
            val id: String,
            val name: String,
            val brand: String,
            val price: Int,
            val originalPrice: Int,
            val discountRate: Int,
            val url: String,
            val imageUrl: String,
            val rating: Double,
            val reviewCount: Int
        )
    }

    companion object {
        private const val DEFAULT_GENDER = "A"
        private const val DEFAULT_SORT = "POPULAR"
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_SIZE = 20
    }
}
