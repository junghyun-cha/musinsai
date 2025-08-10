package com.choa.musinsai.core.provider.crawler.product

import com.choa.musinsai.core.provider.crawler.Gender
import com.choa.musinsai.core.provider.crawler.ShoppingPlatform
import java.math.BigDecimal

data class ProductSearchResponse(

    val products: List<Product> = emptyList(),

    val totalCount: Int = 0,

    val categoryName: String? = null
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
)
