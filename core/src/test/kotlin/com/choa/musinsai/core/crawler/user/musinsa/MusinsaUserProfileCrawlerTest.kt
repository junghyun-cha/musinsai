package com.choa.musinsai.core.crawler.user.musinsa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MusinsaUserProfileCrawlerTest {

    private lateinit var crawler: MusinsaUserProfileCrawler

    @BeforeEach
    fun setUp() {
        crawler = MusinsaUserProfileCrawler()
    }

    @Test
    fun `쿠키 없이 프로필 조회시 null 반환`() {
        // given
        val emptyCookies = emptyMap<String, String>()

        // when
        val result = crawler.getUserProfile(emptyCookies)

        // then
        assertNull(result)
    }

    @Test
    fun `유효하지 않은 쿠키로 프로필 조회시 null 반환`() {
        // given
        val invalidCookies = mapOf(
            "invalid_key" to "invalid_value"
        )

        // when
        val result = crawler.getUserProfile(invalidCookies)

        // then
        assertNull(result)
    }

    @Test
    @Disabled("실제 쿠키가 필요한 통합 테스트")
    fun `유효한 쿠키로 사용자 프로필 조회 성공`() {
        // given
        val validCookies = mapOf(
            "app_atk" to "YOUR_ACTUAL_ACCESS_TOKEN",
            "app_rtk" to "YOUR_ACTUAL_REFRESH_TOKEN"
        )

        // when
        val result = crawler.getUserProfile(validCookies)

        // then
        assertNotNull(result)
        result?.let {
            assertNotNull(it.profile)
            assertTrue(it.profile.nickName.isNotEmpty())
            assertTrue(it.profile.level > 0)
            assertNotNull(it.benefits)
            assertNotNull(it.menus)
            assertTrue(it.menus.isNotEmpty())
        }
    }

    @Test
    @Disabled("실제 쿠키가 필요한 통합 테스트")
    fun `사용자 혜택 정보 파싱 검증`() {
        // given
        val validCookies = mapOf(
            "app_atk" to "YOUR_ACTUAL_ACCESS_TOKEN",
            "app_rtk" to "YOUR_ACTUAL_REFRESH_TOKEN"
        )

        // when
        val result = crawler.getUserProfile(validCookies)

        // then
        assertNotNull(result)
        result?.benefits?.let { benefits ->
            // 적립금 정보 검증
            benefits.points?.let { points ->
                assertTrue(points.title.isNotEmpty())
                assertTrue(points.amount.contains("원"))
                assertTrue(points.linkUrl.isNotEmpty())
            }

            // 쿠폰 정보 검증
            benefits.coupons?.let { coupons ->
                assertTrue(coupons.title.isNotEmpty())
                assertTrue(coupons.amount.contains("장"))
                assertTrue(coupons.linkUrl.isNotEmpty())
            }
        }
    }

    @Test
    @Disabled("실제 쿠키가 필요한 통합 테스트")
    fun `메뉴 아이템 파싱 검증`() {
        // given
        val validCookies = mapOf(
            "app_atk" to "YOUR_ACTUAL_ACCESS_TOKEN",
            "app_rtk" to "YOUR_ACTUAL_REFRESH_TOKEN"
        )

        // when
        val result = crawler.getUserProfile(validCookies)

        // then
        assertNotNull(result)
        result?.menus?.let { menus ->
            assertTrue(menus.isNotEmpty())

            // 주문 내역 메뉴 검증
            val orderMenu = menus.find { it.title == "주문 내역" }
            assertNotNull(orderMenu)
            orderMenu?.let {
                assertEquals("menu", it.type)
                assertNotNull(it.linkUrl)
                assertTrue(it.linkUrl!!.contains("order"))
            }

            // 고객센터 메뉴 검증
            val csMenu = menus.find { it.title == "고객센터" }
            assertNotNull(csMenu)
            csMenu?.let {
                assertEquals("menu", it.type)
                assertNotNull(it.linkUrl)
                assertTrue(it.linkUrl!!.contains("cs"))
            }
        }
    }

    @Test
    @Disabled("실제 쿠키가 필요한 통합 테스트")
    fun `회원 등급 정보 파싱 검증`() {
        // given
        val validCookies = mapOf(
            "app_atk" to "YOUR_ACTUAL_ACCESS_TOKEN",
            "app_rtk" to "YOUR_ACTUAL_REFRESH_TOKEN"
        )

        // when
        val result = crawler.getUserProfile(validCookies)

        // then
        assertNotNull(result)
        result?.let {
            // 롤링 배너에 등급 정보가 포함되어 있는지 검증
            assertTrue(it.rollingBanners.isNotEmpty())
            val gradeBanner = it.rollingBanners.find { banner ->
                banner.text.contains("LV.")
            }
            assertNotNull(gradeBanner)

            // 바텀시트에 등급 정보가 포함되어 있는지 검증
            val gradeBottomSheet = it.bottomSheets.find { sheet ->
                sheet.serviceType == "MEMBERSHIP"
            }
            assertNotNull(gradeBottomSheet)
        }
    }
}
