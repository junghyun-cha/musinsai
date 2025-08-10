package com.choa.musinsai.core.provider.crawler.shop

data class ShopSearchRequest(
    val shopType: String? = "",
    val region: String? = "",
    val language: String = "ko"
)
