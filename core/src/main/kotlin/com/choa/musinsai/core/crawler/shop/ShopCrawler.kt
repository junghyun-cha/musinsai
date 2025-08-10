package com.choa.musinsai.core.crawler.shop

import com.choa.musinsai.core.crawler.shop.musinsa.OfflineShopSearchResult

/**
 * Generic interface for shop crawlers.
 * Minimal contract matching existing MusinsaOfflineShopCrawler behavior.
 */
interface ShopCrawler {
    fun getShops(
        shopType: String? = "",
        region: String? = "",
        language: String = "ko"
    ): OfflineShopSearchResult
}
