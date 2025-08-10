package com.choa.musinsai.core.crawler.auth.musinsa

import com.choa.musinsai.core.crawler.auth.AuthCrawler
import com.choa.musinsai.core.crawler.auth.AuthInfo
import com.choa.musinsai.core.crawler.auth.AuthTokenInfo
import com.choa.musinsai.core.crawler.auth.MemberInfo
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class MusinsaAuthCrawler : AuthCrawler {

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val AUTH_API_URL = "https://my.musinsa.com/api/member/v1/login-status"
        private const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36"
        private const val TIMEOUT_SECONDS = 10L
        val ACCESS_TOKEN_KEY = "app_atk"
        val REFRESH_TOKEN_KEY = "app_rtk"

        // 인증에 필요한 필수 쿠키 키
        private val REQUIRED_COOKIES = setOf(
            ACCESS_TOKEN_KEY, // Access Token
            REFRESH_TOKEN_KEY, // Refresh Token
        )
    }

    private val webClient = WebClient.builder()
        .baseUrl(AUTH_API_URL)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .defaultHeader("Origin", "https://www.musinsa.com")
        .defaultHeader("Referer", "https://www.musinsa.com/")
        .build()

    override fun getAuthInfo(cookies: Map<String, String>): AuthInfo? {
        return try {
            logger.info { "무신사 인증 정보 조회 시작" }

            val cookieString = buildCookieString(cookies)
            val response = fetchAuthInfo(cookieString)

            if (response.data.loggedIn) {
                logger.info { "인증 정보 조회 성공: ${response.data.memberInfo?.nickName}" }
                convertToAuthInfo(response)
            } else {
                logger.warn { "로그인되지 않은 상태입니다" }
                AuthInfo(loggedIn = false, memberInfo = null, authTokenInfo = null)
            }
        } catch (e: WebClientResponseException) {
            logger.error(e) { "무신사 인증 API 호출 실패: status=${e.statusCode}" }
            null
        } catch (e: Exception) {
            logger.error(e) { "인증 정보 조회 중 오류 발생" }
            null
        }
    }

    override fun extractRequiredCookies(allCookies: String): Map<String, String> {
        val cookieMap = mutableMapOf<String, String>()

        allCookies.split(";").forEach { cookie ->
            val parts = cookie.trim().split("=", limit = 2)
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()

                if (key in REQUIRED_COOKIES) {
                    cookieMap[key] = value
                }
            }
        }

        logger.debug { "추출된 필수 쿠키: ${cookieMap.keys}" }
        return cookieMap
    }

    private fun fetchAuthInfo(cookieString: String): MusinsaAuthResponse {
        return webClient.get()
            .header(HttpHeaders.COOKIE, cookieString)
            .retrieve()
            .bodyToMono(MusinsaAuthResponse::class.java)
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .onErrorResume { error ->
                logger.error(error) { "인증 API 호출 중 오류 발생" }
                Mono.just(MusinsaAuthResponse())
            }
            .block() ?: MusinsaAuthResponse()
    }

    private fun buildCookieString(cookies: Map<String, String>): String {
        return cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }
    }

    private fun convertToAuthInfo(response: MusinsaAuthResponse): AuthInfo {
        val memberInfo = response.data.memberInfo?.let {
            MemberInfo(
                level = it.level,
                levelText = it.levelText,
                nickName = it.nickName,
                profileImageUrl = it.profileImageUrl
            )
        }

        val authTokenInfo = response.data.authTokenInfo?.let {
            AuthTokenInfo(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )
        }

        return AuthInfo(
            loggedIn = response.data.loggedIn,
            memberInfo = memberInfo,
            authTokenInfo = authTokenInfo
        )
    }
}
