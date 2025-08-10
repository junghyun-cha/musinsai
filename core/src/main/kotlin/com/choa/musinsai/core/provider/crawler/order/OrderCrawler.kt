package com.choa.musinsai.core.provider.crawler.order

import com.choa.musinsai.core.provider.crawler.order.musinsa.OrderDetailRequest
import com.choa.musinsai.core.provider.crawler.order.musinsa.OrderDetailResponse

interface OrderCrawler {
    suspend fun getOrderDetail(request: OrderDetailRequest): OrderDetailResponse
}
