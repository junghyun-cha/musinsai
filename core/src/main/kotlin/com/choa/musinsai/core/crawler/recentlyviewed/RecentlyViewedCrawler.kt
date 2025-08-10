package com.choa.musinsai.core.crawler.recentlyviewed

interface RecentlyViewedCrawler {
    suspend fun getRecentlyViewedBrands(
        request: RecentlyViewedBrandRequest
    ): RecentlyViewedBrandResponse
}