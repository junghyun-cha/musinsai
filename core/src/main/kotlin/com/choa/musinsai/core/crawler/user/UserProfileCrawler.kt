package com.choa.musinsai.core.crawler.user

interface UserProfileCrawler {
    fun getUserProfile(cookies: Map<String, String>): UserProfileResponse?
}
