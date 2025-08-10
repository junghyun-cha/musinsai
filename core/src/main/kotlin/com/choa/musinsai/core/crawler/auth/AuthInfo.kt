package com.choa.musinsai.core.crawler.auth

data class AuthInfo(
    val loggedIn: Boolean,
    val memberInfo: MemberInfo?,
    val authTokenInfo: AuthTokenInfo?
)

data class MemberInfo(
    val level: Int,
    val levelText: String,
    val nickName: String,
    val profileImageUrl: String?
)

data class AuthTokenInfo(
    val accessToken: String,
    val refreshToken: String
)
