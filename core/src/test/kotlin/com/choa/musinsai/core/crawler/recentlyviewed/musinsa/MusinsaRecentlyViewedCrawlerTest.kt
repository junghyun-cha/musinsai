package com.choa.musinsai.core.crawler.recentlyviewed.musinsa

import com.choa.musinsai.core.config.WebClientConfig
import com.choa.musinsai.core.crawler.recentlyviewed.RecentlyViewedBrandRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest
@Import(WebClientConfig::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MusinsaRecentlyViewedCrawlerTest(
    private val webClient: WebClient
) {
    
    companion object {
        private val logger = LoggerFactory.getLogger(MusinsaRecentlyViewedCrawlerTest::class.java)
    }
    
    private val crawler = MusinsaRecentlyViewedCrawler(webClient)
    
    @Test
    fun `최근 본 브랜드 리스트를 조회할 수 있다`() = runBlocking {
        // given
        val request = RecentlyViewedBrandRequest()
        
        // when
        val response = crawler.getRecentlyViewedBrands(request)
        
        // then
        assertNotNull(response)
        assertNotNull(response.total)
        assertNotNull(response.brandList)
        
        logger.info("총 브랜드 수: ${response.total}")
        
        if (response.brandList.isNotEmpty()) {
            val firstBrand = response.brandList.first()
            logger.info("""
                첫 번째 브랜드 정보:
                - 브랜드 ID: ${firstBrand.brand}
                - 브랜드명: ${firstBrand.brandName}
                - 브랜드명(영문): ${firstBrand.brandNameEng}
                - 이미지: ${firstBrand.brandImage}
                - 링크: ${firstBrand.link}
                - 성별: ${firstBrand.sex}
            """.trimIndent())
            
            assertNotNull(firstBrand.brand)
            assertNotNull(firstBrand.brandName)
            assertNotNull(firstBrand.brandNameEng)
            assertNotNull(firstBrand.link)
        }
    }
    
    @Test
    @EnabledIfEnvironmentVariable(named = "MUSINSA_TEST_COOKIE", matches = ".+")
    fun `쿠키를 사용하여 인증된 사용자의 최근 본 브랜드를 조회할 수 있다`() = runBlocking {
        // 환경변수로 쿠키 설정 (실제 테스트 시)
        val cookieString = System.getenv("MUSINSA_TEST_COOKIE") ?: ""
        val cookies = if (cookieString.isNotBlank()) {
            cookieString.split(";").associate {
                val parts = it.trim().split("=", limit = 2)
                parts[0] to (parts.getOrNull(1) ?: "")
            }
        } else {
            emptyMap()
        }
        
        // given
        val request = RecentlyViewedBrandRequest(cookies = cookies)
        
        // when
        val response = crawler.getRecentlyViewedBrands(request)
        
        // then
        assertNotNull(response)
        assertNotNull(response.total)
        assertNotNull(response.brandList)
        
        logger.info("인증된 사용자의 최근 본 브랜드 수: ${response.total}")
        
        response.brandList.forEach { brand ->
            logger.info("브랜드: ${brand.brandName}(${brand.brandNameEng})")
        }
    }
}