package com.choa.musinsai.core.provider.crawler.order.musinsa

data class OrderDetailRequest(
    val orderNo: String,
    val cookies: Map<String, String>? = null
)
