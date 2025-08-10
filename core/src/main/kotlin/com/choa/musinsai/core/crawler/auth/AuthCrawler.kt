package com.choa.musinsai.core.crawler.auth

interface AuthCrawler {
    fun getAuthInfo(cookies: Map<String, String>): AuthInfo?
    fun extractRequiredCookies(allCookies: String): Map<String, String>
}
