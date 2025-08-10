package com.choa.musinsai.core.provider.crawler.recentlyviewed.musinsa

import com.choa.musinsai.core.provider.crawler.auth.musinsa.MusinsaAuthCrawler
import com.choa.musinsai.core.provider.crawler.recentlyviewed.RecentlyViewedBrandRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest
@ActiveProfiles("test")
class MusinsaRecentlyViewedCrawlerTest(

) {

    companion object {
        private val logger = LoggerFactory.getLogger(MusinsaRecentlyViewedCrawlerTest::class.java)
    }

    @Autowired
    private lateinit var crawler: MusinsaRecentlyViewedCrawler

    val cookies = mutableMapOf<String, String>()

    @BeforeEach
    fun setUp() {

        cookies.put(MusinsaAuthCrawler.REFRESH_TOKEN_KEY, "63bbf83ebbb90e022731735d9b0776d8ad61bf5b")
        cookies.put(
            MusinsaAuthCrawler.ACCESS_TOKEN_KEY,
            "sJ5K8RVvBw6SQT5kHCkkCMvJ6Gbqe6n4b8XX5hlxpmfg6c%2Fts41qwlhCo5TWxyirpUG5DaxBsM1880LVuSiSdpkHBkvBnZdf5YKSV9VdxcoSMAYeHrt5rVCp0af3kNzKEmuwcvIuqefae2fyWZ1n8gt6R1rYK8lihEF7eOmc%2B9rNDwTPN9JLJVCDisDhqslzTT7GfKtMd82PHR5gVyIkaZJc6I8oip7AvQwkm5GtoA%2FCLS7bPAjHRSmtlVacyh6de%2Fq11kkC37QXWsCMWWcAmS9ra7W16qBa5tE%2F0opuz77G0Q%2FKkfazlndcKE1RDnqUNl%2FIjri406tLpicSFETfh6kX%2FfSaz08dUbUcxCuBcovyGErMyn%2BDEcvHPfabpdwq3kJZjIiCa96jNCrNa0ZMXXEBy4AcbOCrR016vzGldBV8xqPOak8nTeQ7IEx6vxdNr8D6pmdAFdGelvZnjsViYqr1a4llINPwfGpwxzqBqFldv8Fs%2BUnoBLuRP7yBiF%2FXv9zsMIc5eXFcbFzwulEpRq%2BSNFz07Yerog%2FAfF%2FIWO93B9qY%2FgeZmOc4X6AV3z1QM2R7wNoshGayu152HNzTVz%2FnscoGmUIdmBbuon6O8Pw8zjUZpe18hfnkJzmo%2FFclCv75%2FDwBd05hD2t2ysL25bH8E5GaNvrrsLFdLoO02NeMglPk%2F2kO1EZHGTlbgpR97XgHaHEkhCuQvAuu3nlmLNqKNU6lslYufmapwJIlo9bISR8PThE6QP%2B1PUMaSau1"
        )
    }

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
            logger.info(
                """
                첫 번째 브랜드 정보:
                - 브랜드 ID: ${firstBrand.brand}
                - 브랜드명: ${firstBrand.brandName}
                - 브랜드명(영문): ${firstBrand.brandNameEng}
                - 이미지: ${firstBrand.brandImage}
                - 링크: ${firstBrand.link}
                - 성별: ${firstBrand.sex}
                """.trimIndent()
            )

            assertNotNull(firstBrand.brand)
            assertNotNull(firstBrand.brandName)
            assertNotNull(firstBrand.brandNameEng)
            assertNotNull(firstBrand.link)
        }
    }

    @Test
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
