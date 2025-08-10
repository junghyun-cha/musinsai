package com.choa.musinsai.core.provider.crawler.user.musinsa

import com.choa.musinsai.core.provider.crawler.user.BannerContent
import com.choa.musinsai.core.provider.crawler.user.BenefitItem
import com.choa.musinsai.core.provider.crawler.user.BottomSheet
import com.choa.musinsai.core.provider.crawler.user.BottomSheetButton
import com.choa.musinsai.core.provider.crawler.user.NudgeBanner
import com.choa.musinsai.core.provider.crawler.user.NudgeBannerContent
import com.choa.musinsai.core.provider.crawler.user.ReviewBenefit
import com.choa.musinsai.core.provider.crawler.user.RollingBanner
import com.choa.musinsai.core.provider.crawler.user.UserBenefits
import com.choa.musinsai.core.provider.crawler.user.UserMenuItem
import com.choa.musinsai.core.provider.crawler.user.UserProfile
import com.choa.musinsai.core.provider.crawler.user.UserProfileCrawler
import com.choa.musinsai.core.provider.crawler.user.UserProfileResponse
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class MusinsaUserProfileCrawler(
    webClientBuilder: WebClient.Builder
) : UserProfileCrawler {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val MY_MENU_API_URL = "https://my.musinsa.com/api2/member/v5/my-menu"
        private const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36"
        private const val TIMEOUT_SECONDS = 10L

        // 필수 쿠키 키
        private val REQUIRED_COOKIES = setOf(
            "app_atk", // Access Token
            "app_rtk", // Refresh Token
        )
    }

    private val webClient = webClientBuilder
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .defaultHeader("Origin", "https://www.musinsa.com")
        .defaultHeader("Referer", "https://www.musinsa.com/")
        .build()

    override fun getUserProfile(cookies: Map<String, String>): UserProfileResponse? {
        return try {
            logger.info { "무신사 사용자 프로필 정보 조회 시작" }

            if (!validateCookies(cookies)) {
                logger.warn { "필수 쿠키가 누락되었습니다" }
                return null
            }

            val cookieString = buildCookieString(cookies)
            val response = fetchUserProfile(cookieString)

            if (response.meta.result == "SUCCESS") {
                logger.info { "사용자 프로필 조회 성공: ${response.data.profile?.nickName}" }
                convertToUserProfileResponse(response)
            } else {
                logger.warn { "사용자 프로필 조회 실패: ${response.meta.message}" }
                null
            }
        } catch (e: WebClientResponseException) {
            logger.error(e) { "무신사 마이메뉴 API 호출 실패: status=${e.statusCode}" }
            null
        } catch (e: Exception) {
            logger.error(e) { "사용자 프로필 정보 조회 중 오류 발생" }
            null
        }
    }

    private fun validateCookies(cookies: Map<String, String>): Boolean {
        val hasRequiredCookies = REQUIRED_COOKIES.all { cookies.containsKey(it) }
        if (!hasRequiredCookies) {
            logger.warn { "필수 쿠키 누락: ${REQUIRED_COOKIES - cookies.keys}" }
        }
        return hasRequiredCookies
    }

    private fun fetchUserProfile(cookieString: String): MusinsaMyMenuResponse {
        val timestamp = System.currentTimeMillis()
        val urlWithTimestamp = "$MY_MENU_API_URL?_ts=$timestamp"

        return webClient.get()
            .uri(urlWithTimestamp)
            .header(HttpHeaders.COOKIE, cookieString)
            .retrieve()
            .bodyToMono(MusinsaMyMenuResponse::class.java)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .onErrorResume { error ->
                logger.error(error) { "마이메뉴 API 호출 중 오류 발생" }
                Mono.just(MusinsaMyMenuResponse())
            }
            .block() ?: MusinsaMyMenuResponse()
    }

    private fun buildCookieString(cookies: Map<String, String>): String {
        return cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }
    }

    private fun convertToUserProfileResponse(response: MusinsaMyMenuResponse): UserProfileResponse {
        val profile = response.data.profile?.let {
            UserProfile(
                hashId = it.hashId,
                level = it.level,
                nickName = it.nickName,
                profileImageUrl = it.profileImageUrl,
                profileSettingUrl = it.profileSettingUrl,
                brandClubTitle = it.brandClubTitle,
                brandClubRankText = it.brandClubRankText,
                brandClubBadgeImageUrl = it.brandClubBadgeImageUrl,
                brandClubUrl = it.brandClubUrl
            )
        } ?: return createEmptyProfile()

        val benefits = parseBenefits(response.data.shortcut)
        val menus = parseMenus(response.data.menus)
        val bottomSheets = parseBottomSheets(response.data.bottomSheets)
        val rollingBanners = parseRollingBanners(response.data.rollingBanners)
        val nudgeBanner = parseNudgeBanner(response.data.nudgeBanner)

        return UserProfileResponse(
            profile = profile,
            benefits = benefits,
            menus = menus,
            bottomSheets = bottomSheets,
            rollingBanners = rollingBanners,
            nudgeBanner = nudgeBanner
        )
    }

    private fun createEmptyProfile(): UserProfileResponse {
        return UserProfileResponse(
            profile = UserProfile(
                hashId = "",
                level = 0,
                nickName = "",
                profileImageUrl = null,
                profileSettingUrl = "",
                brandClubTitle = null,
                brandClubRankText = null,
                brandClubBadgeImageUrl = null,
                brandClubUrl = null
            ),
            benefits = UserBenefits(null, null, null, null),
            menus = emptyList(),
            bottomSheets = emptyList(),
            rollingBanners = emptyList(),
            nudgeBanner = null
        )
    }

    private fun parseBenefits(shortcut: MusinsaShortcut?): UserBenefits {
        if (shortcut == null) {
            return UserBenefits(null, null, null, null)
        }

        var points: BenefitItem? = null
        var musinsaMoney: BenefitItem? = null
        var coupons: BenefitItem? = null

        shortcut.horizontal.forEach { item ->
            when (item.item) {
                "point" -> points = BenefitItem(
                    title = item.title,
                    amount = item.amount ?: "0원",
                    linkUrl = item.linkUrl
                )

                "musinsaMoney" -> musinsaMoney = BenefitItem(
                    title = item.title,
                    amount = item.amount ?: "충전하기",
                    linkUrl = item.linkUrl
                )

                "coupon" -> coupons = BenefitItem(
                    title = item.title,
                    amount = item.amount ?: "0장",
                    linkUrl = item.linkUrl
                )
            }
        }

        val writableReviews = shortcut.vertical?.let {
            if (it.item == "review") {
                ReviewBenefit(
                    title = it.title,
                    subTitle = it.subTitle ?: "",
                    linkUrl = it.linkUrl
                )
            } else null
        }

        return UserBenefits(points, musinsaMoney, coupons, writableReviews)
    }

    private fun parseMenus(menus: List<MusinsaMenu>): List<UserMenuItem> {
        return menus.mapNotNull { menu ->
            if (menu.type == "divider") {
                null
            } else {
                UserMenuItem(
                    type = menu.type,
                    title = menu.title,
                    subTitle = menu.subTitle,
                    linkUrl = menu.linkUrl,
                    buttonName = menu.buttonName,
                    existNewBadge = menu.existNewBadge,
                    contents = menu.contents?.map { content ->
                        BannerContent(
                            title = content.title,
                            subTitle1 = content.subTitle1,
                            linkUrl = content.linkUrl,
                            imageUrl = content.imageUrl,
                            startDate = content.startDate,
                            endDate = content.endDate,
                            buttonName = content.buttonName
                        )
                    }
                )
            }
        }
    }

    private fun parseBottomSheets(bottomSheets: List<MusinsaBottomSheet>): List<BottomSheet> {
        return bottomSheets.map { sheet ->
            BottomSheet(
                id = sheet.id,
                title = sheet.title,
                description = sheet.description,
                descriptionArgs = sheet.descriptionArgs,
                buttons = sheet.buttons.map { button ->
                    BottomSheetButton(
                        buttonName = button.buttonName,
                        linkUrl = button.linkUrl
                    )
                },
                imageUrl = sheet.imageUrl,
                type = sheet.type,
                serviceType = sheet.serviceType,
                repeatType = sheet.repeatType
            )
        }
    }

    private fun parseRollingBanners(banners: List<MusinsaRollingBanner>): List<RollingBanner> {
        return banners.map { banner ->
            RollingBanner(
                text = banner.text,
                args = banner.args,
                linkUrl = banner.linkUrl
            )
        }
    }

    private fun parseNudgeBanner(nudgeBanner: MusinsaNudgeBanner?): NudgeBanner? {
        return nudgeBanner?.let {
            NudgeBanner(
                contents = it.contents.map { content ->
                    NudgeBannerContent(
                        title = content.title,
                        serviceType = content.serviceType,
                        subTitle = content.subTitle,
                        linkUrl = content.linkUrl,
                        imageUrl = content.imageUrl,
                        titleColor = content.titleColor,
                        subTitleColor = content.subTitleColor,
                        iconTintColor = content.iconTintColor,
                        backgroundColor = content.backgroundColor
                    )
                }
            )
        }
    }
}
