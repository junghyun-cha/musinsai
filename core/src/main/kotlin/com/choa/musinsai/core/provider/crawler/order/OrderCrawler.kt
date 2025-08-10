package com.choa.musinsai.core.provider.crawler.order

import com.choa.musinsai.core.crawler.order.OrderHistoryRequest
import com.choa.musinsai.core.crawler.order.OrderHistoryResponse

interface OrderCrawler {
    suspend fun getOrderHistory(
        request: OrderHistoryRequest,
        cookies: Map<String, String>?
    ): OrderHistoryResponse
}