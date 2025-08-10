package com.choa.musinsai.core.crawler.shop

data class ShopDetailRequest(
    val shopNo: Long,
    val language: String = "ko"
)
