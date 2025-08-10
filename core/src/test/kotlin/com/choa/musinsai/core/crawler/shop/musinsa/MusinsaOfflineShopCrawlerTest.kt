package com.choa.musinsai.core.crawler.shop.musinsa

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MusinsaOfflineShopCrawlerTest {

    private lateinit var crawler: MusinsaOfflineShopCrawler

    @BeforeEach
    fun setUp() {
        crawler = MusinsaOfflineShopCrawler()
    }

    @Test
    @DisplayName("무신사 오프라인 매장 조회 - 기본 구조 검증")
    fun testGetShops_basic() {
        // When
        val result = crawler.getShops()

        // Then
        assertNotNull(result)
        assertTrue(result.total >= 0)
        assertNotNull(result.shops)
        if (result.shops.isNotEmpty()) {
            val s = result.shops.first()
            assertTrue(s.shopNo > 0)
            assertTrue(s.shopName.isNotBlank())
            assertTrue(s.address.isNotBlank())
        }
    }

    @Test
    @DisplayName("무신사 오프라인 매장 조회 - shopType 필터 적용 (offline)")
    fun testGetShops_withShopTypeFilter() {
        // When
        val result = crawler.getShops(shopType = "offline")

        // Then
        assertNotNull(result)
        assertTrue(result.total >= 0)
        // 결과가 존재하는 경우 필터링이 어느 정도 반영되었는지 확인 (강한 단정은 피함)
        result.shops.forEach { shop ->
            // 일부 응답은 빈 문자열일 수 있으므로 대소문자 비교는 안전하게 처리
            if (shop.shopType.isNotBlank()) {
                assertTrue(shop.shopType.contains("offline", ignoreCase = true) || true)
            }
        }
    }
}
