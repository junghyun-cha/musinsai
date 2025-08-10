package com.choa.musinsai.core.provider.crawler.shop

data class ShopDetailRequest(
    val shopNo: Long,
    val language: String = "ko"
)
