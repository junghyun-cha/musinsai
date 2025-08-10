package com.choa.musinsai.core.crawler.shop

// Public result models (simple, decoupled from API schema if needed later)
data class Shop(
    val shopNo: Long,
    val shopType: String,
    val shopName: String,
    val address: String
)

data class ShopSearchResult(
    val total: Int,
    val shops: List<Shop>
)
