package com.choa.musinsai.core.provider.crawler.user.musinsa

import com.choa.musinsai.core.provider.crawler.auth.musinsa.MusinsaAuthCrawler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class MusinsaUserProfileCrawlerTest {
    @Autowired
    private lateinit var sut: MusinsaUserProfileCrawler

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
    fun `쿠키 없이 프로필 조회시 null 반환`() {
        // given
        val emptyCookies = emptyMap<String, String>()

        // when
        val result = sut.getUserProfile(emptyCookies)

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
        val result = sut.getUserProfile(invalidCookies)

        // then
        assertNull(result)
    }

    @Test
    fun `유효한 쿠키로 사용자 프로필 조회 성공`() {
        // given


        // when
        val result = sut.getUserProfile(cookies)

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
    fun `사용자 혜택 정보 파싱 검증`() {
        // given


        // when
        val result = sut.getUserProfile(cookies)

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
    fun `메뉴 아이템 파싱 검증`() {


        // when
        val result = sut.getUserProfile(cookies)

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
    fun `회원 등급 정보 파싱 검증`() {


        // when
        val result = sut.getUserProfile(cookies)

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
