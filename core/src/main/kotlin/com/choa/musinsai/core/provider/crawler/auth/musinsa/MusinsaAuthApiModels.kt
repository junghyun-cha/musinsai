package com.choa.musinsai.core.provider.crawler.auth.musinsa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaAuthResponse(
    @JsonProperty("data")
    val data: MusinsaAuthData = MusinsaAuthData()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaAuthData(
    @JsonProperty("loggedIn")
    val loggedIn: Boolean = false,

    @JsonProperty("memberInfo")
    val memberInfo: MusinsaMemberInfo? = null,

    @JsonProperty("authTokenInfo")
    val authTokenInfo: MusinsaAuthTokenInfo? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaMemberInfo(
    @JsonProperty("level")
    val level: Int = 0,

    @JsonProperty("levelText")
    val levelText: String = "",

    @JsonProperty("nickName")
    val nickName: String = "",

    @JsonProperty("profileImageUrl")
    val profileImageUrl: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaAuthTokenInfo(
    @JsonProperty("accessToken")
    val accessToken: String = "",

    @JsonProperty("refreshToken")
    val refreshToken: String = ""
)
