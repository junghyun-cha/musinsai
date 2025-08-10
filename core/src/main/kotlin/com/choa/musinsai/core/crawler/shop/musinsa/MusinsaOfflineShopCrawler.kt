package com.choa.musinsai.core.crawler.shop.musinsa

import com.choa.musinsai.core.crawler.shop.Shop
import com.choa.musinsai.core.crawler.shop.ShopCrawler
import com.choa.musinsai.core.crawler.shop.ShopDetail
import com.choa.musinsai.core.crawler.shop.ShopDetailRequest
import com.choa.musinsai.core.crawler.shop.ShopDetailResult
import com.choa.musinsai.core.crawler.shop.ShopFloorInfo
import com.choa.musinsai.core.crawler.shop.ShopSearchRequest
import com.choa.musinsai.core.crawler.shop.ShopSearchResult
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
    fun getShops(): ShopSearchResult = getShops(ShopSearchRequest())
    fun getShops(shopType: String?): ShopSearchResult = getShops(ShopSearchRequest(shopType = shopType))
    fun getShops(shopType: String?, region: String?, language: String): ShopSearchResult =
        getShops(ShopSearchRequest(shopType = shopType, region = region, language = language))

    fun getShopDetail(shopNo: Long, language: String = "ko"): ShopDetailResult =
        getShopDetail(ShopDetailRequest(shopNo = shopNo, language = language))

    override fun getShops(request: ShopSearchRequest): ShopSearchResult {
        return try {
            logger.info { "무신사 오프라인 매장 조회 시작: shopType=${request.shopType}, region=${request.region}, language=${request.language}" }
            val res = callApi(request)
            val shops = res.data.list.map { Shop(it.shopNo, it.shopType, it.shopName, it.address) }
            ShopSearchResult(total = res.data.total, shops = shops)
        } catch (e: Exception) {
            logger.error(e) { "무신사 오프라인 매장 조회 중 오류 발생" }
            ShopSearchResult(total = 0, shops = emptyList())
        }
    }

    override fun getShopDetail(request: ShopDetailRequest): ShopDetailResult {
        return try {
            logger.info { "무신사 오프라인 매장 상세 조회 시작: shopNo=${'$'}{request.shopNo}, language=${'$'}{request.language}" }
            val res = callDetailApi(request)
            val d = res.data.detail
            val mapped = ShopDetail(
                shopName = d.shopName,
                shopType = d.shopType,
                address = d.address,
                phone = d.phone ?: emptyList(),
                operationHours = d.operationHours,
                paymentServices = d.paymentServices ?: emptyList(),
                floorInfo = d.floorInfo.map { ShopFloorInfo(it.floor, it.description) },
                eventPageURL = d.eventPageURL,
                pcMapURL = d.pcMapURL,
                backgroundImageURL = d.backgroundImageURL,
                mmapURL = d.mmapURL
            )
            ShopDetailResult(detail = mapped)
        } catch (e: Exception) {
            logger.error(e) { "무신사 오프라인 매장 상세 조회 중 오류 발생" }
            ShopDetailResult(detail = null)
        }
    }

    private fun callApi(request: ShopSearchRequest): MusinsaOfflineShopApiResponse {
        return webClient.get()
            .uri { uriBuilder ->
                var b = uriBuilder
                request.shopType?.let { b = b.queryParam("shopType", it) }
                request.region?.let { b = b.queryParam("region", it) }
                b = b.queryParam("language", request.language)
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

    private fun callDetailApi(request: ShopDetailRequest): MusinsaOfflineShopDetailApiResponse {
        return webClient.get()
            .uri { b ->
                b.pathSegment(request.shopNo.toString(), "detail")
                    .queryParam("language", request.language)
                    .build()
            }
            .retrieve()
            .bodyToMono(MusinsaOfflineShopDetailApiResponse::class.java)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .onErrorResume { err ->
                logger.error(err) { "오프라인 매장 상세 API 호출 오류" }
                Mono.just(MusinsaOfflineShopDetailApiResponse())
            }
            .block() ?: MusinsaOfflineShopDetailApiResponse()
    }
}
