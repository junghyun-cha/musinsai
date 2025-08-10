package com.choa.musinsai.core.provider.crawler.user

interface UserProfileCrawler {
    fun getUserProfile(cookies: Map<String, String>): UserProfileResponse?
}
