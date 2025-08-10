package com.choa.musinsai.core.crawler.shop

data class ShopSearchRequest(
    val shopType: String? = "",
    val region: String? = "",
    val language: String = "ko"
)
