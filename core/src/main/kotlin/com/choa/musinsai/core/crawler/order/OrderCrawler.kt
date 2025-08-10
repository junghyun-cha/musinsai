package com.choa.musinsai.core.crawler.order

interface OrderCrawler {
    suspend fun getOrderDetail(request: OrderDetailRequest): OrderDetailResponse
}