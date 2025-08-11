package com.choa.musinsai.core.provider.crawler.product

import com.choa.musinsai.core.provider.crawler.Gender
import com.choa.musinsai.core.provider.crawler.ShoppingPlatform
import java.math.BigDecimal

data class ProductSearchResponse(

    val products: List<Product> = emptyList(),

    val totalCount: Int = 0,

    val categoryName: String? = null,

    val currentPage: Int = 1,
    val totalPages: Int = 1
)

data class Product(
    val id: String,
    val name: String,
    val brandName: String,
    val imageUrl: String,
    val productUrl: String,
    val originalPrice: BigDecimal,
    val salePrice: BigDecimal,
    val discountRate: Int,
    val gender: Gender,
    val isSoldOut: Boolean,
    val reviewCount: Int,
    val reviewScore: Double,
    val tags: List<String> = emptyList(),
    val platform: ShoppingPlatform
) {
    // Secondary constructor to help tests create Product with simpler fields
    constructor(
        id: String,
        name: String,
        brand: String,
        price: Int,
        originalPrice: Int,
        discountRate: Int,
        url: String,
        imageUrl: String,
        rating: Double,
        reviewCount: Int
    ) : this(
        id = id,
        name = name,
        brandName = brand,
        imageUrl = imageUrl,
        productUrl = url,
        originalPrice = BigDecimal.valueOf(originalPrice.toLong()),
        salePrice = BigDecimal.valueOf(price.toLong()),
        discountRate = discountRate,
        gender = Gender.UNISEX,
        isSoldOut = false,
        reviewCount = reviewCount,
        reviewScore = rating,
        tags = emptyList(),
        platform = ShoppingPlatform.MUSINSA
    )
}
