package com.choa.musinsai.core.provider.crawler.recentlyviewed.musinsa

import com.choa.musinsai.core.exception.CrawlerException
import com.choa.musinsai.core.provider.crawler.recentlyviewed.RecentlyViewedBrand
import com.choa.musinsai.core.provider.crawler.recentlyviewed.RecentlyViewedBrandRequest
import com.choa.musinsai.core.provider.crawler.recentlyviewed.RecentlyViewedBrandResponse
import com.choa.musinsai.core.provider.crawler.recentlyviewed.RecentlyViewedCrawler
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class MusinsaRecentlyViewedCrawler(
    private val webClient: WebClient
) : RecentlyViewedCrawler {

    companion object {
        private val logger = LoggerFactory.getLogger(MusinsaRecentlyViewedCrawler::class.java)
        private const val BASE_URL = "https://www.musinsa.com"
        private const val RECENTLY_VIEWED_BRANDS_API = "/app/api/recent/get_brand_list"
    }

    override suspend fun getRecentlyViewedBrands(
        request: RecentlyViewedBrandRequest
    ): RecentlyViewedBrandResponse {
        logger.info("무신사 최근 본 브랜드 리스트 조회 시작")

        return try {
            val response = webClient
                .get()
                .uri("$BASE_URL$RECENTLY_VIEWED_BRANDS_API")
                .apply {
                    // 쿠키가 있으면 추가
                    request.cookies?.forEach { (name, value) ->
                        cookie(name, value)
                    }
                }
                .retrieve()
                .bodyToMono(MusinsaRecentlyViewedBrandApiResponse::class.java)
                .onErrorResume { error ->
                    logger.error("무신사 최근 본 브랜드 조회 실패: ${error.message}", error)
                    Mono.error(CrawlerException("무신사 최근 본 브랜드 조회 실패: ${error.message}"))
                }
                .awaitSingle()

            if (response.code != 200) {
                throw CrawlerException("무신사 최근 본 브랜드 조회 실패: ${response.message}")
            }

            RecentlyViewedBrandResponse(
                total = response.data.total,
                brandList = response.data.brandList.map { brand ->
                    RecentlyViewedBrand(
                        brand = brand.brand,
                        brandName = brand.brandNm,
                        brandNameEng = brand.brandNmEng,
                        brandImage = brand.brandImg,
                        link = brand.link,
                        sex = brand.sex
                    )
                }
            ).also {
                logger.info("무신사 최근 본 브랜드 ${it.brandList.size}개 조회 완료")
            }
        } catch (e: CrawlerException) {
            throw e
        } catch (e: Exception) {
            logger.error("무신사 최근 본 브랜드 조회 중 예기치 않은 오류 발생", e)
            throw CrawlerException("무신사 최근 본 브랜드 조회 실패: ${e.message}")
        }
    }
}
