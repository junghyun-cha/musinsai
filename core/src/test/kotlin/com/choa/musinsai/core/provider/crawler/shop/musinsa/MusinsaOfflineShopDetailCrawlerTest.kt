package com.choa.musinsai.core.provider.crawler.shop.musinsa

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class MusinsaOfflineShopDetailCrawlerTest {

    @Autowired
    private lateinit var crawler: MusinsaOfflineShopCrawler

    @Test
    @DisplayName("무신사 오프라인 매장 상세 조회 - 기본 구조 검증")
    fun testGetShopDetail_basic() {
        // Given
        val shopNo = 25L

        // When
        val result = crawler.getShopDetail(shopNo)

        // Then
        assertNotNull(result)
        val detail = result.detail
        assertNotNull(detail)
        if (detail != null) {
            assertTrue(detail.shopName.isNotBlank())
            assertTrue(detail.address.isNotBlank())
            // Optional fields presence checks
            detail.floorInfo.forEach {
                assertTrue(it.floor.isNotBlank())
            }
        }
    }
}
