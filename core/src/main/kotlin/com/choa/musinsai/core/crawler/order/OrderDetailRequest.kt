package com.choa.musinsai.core.crawler.order

data class OrderDetailRequest(
    val orderNo: String,
    val accessToken: String? = null
)