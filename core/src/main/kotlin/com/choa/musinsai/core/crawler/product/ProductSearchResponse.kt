package com.choa.musinsai.core.crawler.product

import com.choa.musinsai.core.domain.Product

data class ProductSearchResponse(

    val products: List<Product> = emptyList(),

    val totalCount: Int = 0,

    val categoryName: String? = null
)
