package com.choa.musinsai.core.crawler.shop

/**
 * Generic interface for shop crawlers.
 * Request/Response standardized to objects.
 */
interface ShopCrawler {
    fun getShops(request: ShopSearchRequest): ShopSearchResult
    fun getShopDetail(request: ShopDetailRequest): ShopDetailResult
}
