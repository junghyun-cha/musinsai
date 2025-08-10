package com.choa.musinsai.core.provider.crawler.recentlyviewed

interface RecentlyViewedCrawler {
    suspend fun getRecentlyViewedBrands(
        request: RecentlyViewedBrandRequest
    ): RecentlyViewedBrandResponse
}
