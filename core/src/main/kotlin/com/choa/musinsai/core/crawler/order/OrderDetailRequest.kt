package com.choa.musinsai.core.crawler.order

data class OrderDetailRequest(
    val orderNo: String,
    val cookies: Map<String, String>? = null
)
