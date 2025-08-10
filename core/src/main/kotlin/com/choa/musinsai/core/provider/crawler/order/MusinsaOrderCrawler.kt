package com.choa.musinsai.core.provider.crawler.order

import com.choa.musinsai.core.crawler.order.OrderHistoryRequest
import com.choa.musinsai.core.crawler.order.OrderHistoryResponse
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class MusinsaOrderCrawler(
    private val webClientBuilder: WebClient.Builder
) : OrderCrawler {
    
    companion object {
        private val logger = LoggerFactory.getLogger(MusinsaOrderCrawler::class.java)
        private const val ORDER_HISTORY_URL = "https://api.musinsa.com/api2/claim/store/mypage/integration/order"
    }
    
    override suspend fun getOrderHistory(
        request: OrderHistoryRequest,
        cookies: Map<String, String>?
    ): OrderHistoryResponse {
        logger.info("무신사 주문 내역 크롤링 시작")
        
        val url = buildOrderHistoryUrl(request)
        logger.info("주문 내역 URL: $url")
        
        return try {
            val webClient = webClientBuilder.build()
            
            val requestSpec = webClient.get()
                .uri(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .header("Accept", "application/json")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8")
            
            // 쿠키 추가
            cookies?.let { cookieMap ->
                val cookieString = cookieMap.entries.joinToString("; ") { "${it.key}=${it.value}" }
                if (cookieString.isNotEmpty()) {
                    requestSpec.header("Cookie", cookieString)
                    logger.debug("쿠키 추가됨")
                }
            }
            
            val orderHistory = requestSpec
                .retrieve()
                .bodyToMono(OrderHistoryResponse::class.java)
                .onErrorResume { error ->
                    logger.error("무신사 주문 내역 크롤링 실패: ${error.message}", error)
                    Mono.just(OrderHistoryResponse())
                }
                .awaitSingle()
            
            logger.info("무신사 주문 내역 크롤링 성공 - 주문 수: ${orderHistory.data?.size ?: 0}")
            orderHistory
        } catch (e: Exception) {
            logger.error("무신사 주문 내역 크롤링 실패", e)
            OrderHistoryResponse()
        }
    }
    
    private fun buildOrderHistoryUrl(request: OrderHistoryRequest): String {
        val params = mutableListOf<String>()
        
        params.add("size=${request.size}")
        params.add("searchText=${request.searchText ?: ""}")
        params.add("startDate=${request.startDate}")
        params.add("endDate=${request.endDate}")
        
        return "$ORDER_HISTORY_URL?${params.joinToString("&")}"
    }
}