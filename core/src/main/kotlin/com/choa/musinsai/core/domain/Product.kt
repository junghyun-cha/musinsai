package com.choa.musinsai.core.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: String,
    val name: String,
    val brandName: String,
    val brandId: String? = null,
    val imageUrl: String,
    val thumbnailUrl: String? = null,
    val productUrl: String,
    val originalPrice: BigDecimal,
    val salePrice: BigDecimal,
    val discountRate: Int = 0,
    val category: String? = null,
    val subCategory: String? = null,
    val gender: Gender = Gender.UNISEX,
    val isSoldOut: Boolean = false,
    val reviewCount: Int = 0,
    val reviewScore: Double = 0.0,
    val tags: List<String> = emptyList(),
    val platform: ShoppingPlatform,
    val additionalInfo: Map<String, Any> = emptyMap(),
    val crawledAt: LocalDateTime = LocalDateTime.now()
) {
    val hasDiscount: Boolean
        get() = discountRate > 0
}

enum class Gender(val displayName: String) {
    MALE("남성"),
    FEMALE("여성"),
    UNISEX("공용"),
    KIDS("키즈")
}

enum class ShoppingPlatform(val displayName: String, val baseUrl: String) {
    MUSINSA("무신사", "https://www.musinsa.com"),
    TWENTYNINE_CM("29CM", "https://www.29cm.co.kr"),
    W_CONCEPT("W컨셉", "https://www.wconcept.co.kr"),
    SSF_SHOP("SSF샵", "https://www.ssfshop.com"),
    KREAM("크림", "https://kream.co.kr")
}

data class SearchResult(
    val products: List<Product>,
    val totalCount: Int,
    val currentPage: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val searchKeyword: String? = null,
    val filters: Map<String, Any> = emptyMap()
)

data class ProductDetail(
    val product: Product,
    val description: String? = null,
    val sizeOptions: List<SizeOption> = emptyList(),
    val colorOptions: List<ColorOption> = emptyList(),
    val images: List<String> = emptyList(),
    val materials: String? = null,
    val careInstructions: String? = null,
    val relatedProducts: List<Product> = emptyList()
)

data class SizeOption(
    val size: String,
    val isAvailable: Boolean,
    val stock: Int? = null
)

data class ColorOption(
    val color: String,
    val colorCode: String? = null,
    val imageUrl: String? = null,
    val isAvailable: Boolean
)
