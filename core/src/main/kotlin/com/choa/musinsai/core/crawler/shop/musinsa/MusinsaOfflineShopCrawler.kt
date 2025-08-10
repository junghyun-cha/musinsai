package com.choa.musinsai.core.crawler.shop.musinsa

import com.choa.musinsai.core.crawler.shop.ShopCrawler
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration

class MusinsaOfflineShopCrawler : ShopCrawler {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val BASE_URL = "https://api.musinsa.com/api2/campaign/offline/v1/shops"
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        private const val TIMEOUT_SECONDS = 30L
    }

    private val webClient = WebClient.builder()
        .baseUrl(BASE_URL)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .build()

    // Convenience overloads to preserve default-like usage when calling the concrete class directly
    fun getShops(): OfflineShopSearchResult = getShops("", "", "ko")
    fun getShops(shopType: String?): OfflineShopSearchResult = getShops(shopType, "", "ko")

    override fun getShops(
        shopType: String?,
        region: String?,
        language: String
    ): OfflineShopSearchResult {
        return try {
            logger.info { "무신사 오프라인 매장 조회 시작: shopType=$shopType, region=$region, language=$language" }
            val res = callApi(shopType, region, language)
            val shops = res.data.list.map { OfflineShop(it.shopNo, it.shopType, it.shopName, it.address) }
            OfflineShopSearchResult(total = res.data.total, shops = shops)
        } catch (e: Exception) {
            logger.error(e) { "무신사 오프라인 매장 조회 중 오류 발생" }
            OfflineShopSearchResult(total = 0, shops = emptyList())
        }
    }

    private fun callApi(shopType: String?, region: String?, language: String): MusinsaOfflineShopApiResponse {
        return webClient.get()
            .uri { uriBuilder ->
                var b = uriBuilder
                if (shopType != null) b = b.queryParam("shopType", shopType)
                if (region != null) b = b.queryParam("region", region)
                b = b.queryParam("language", language)
                b.build()
            }
            .retrieve()
            .bodyToMono(MusinsaOfflineShopApiResponse::class.java)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .onErrorResume { err ->
                logger.error(err) { "오프라인 매장 API 호출 오류" }
                Mono.just(MusinsaOfflineShopApiResponse())
            }
            .block() ?: MusinsaOfflineShopApiResponse()
    }
}
