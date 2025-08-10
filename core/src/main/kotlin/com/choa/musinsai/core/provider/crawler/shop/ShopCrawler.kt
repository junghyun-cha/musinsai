package com.choa.musinsai.core.provider.crawler.shop

/**
 * Generic interface for shop crawlers.
 * Request/Response standardized to objects.
 */
interface ShopCrawler {
    fun getShops(request: ShopSearchRequest): ShopSearchResponse
    fun getShopDetail(request: ShopDetailRequest): ShopDetailResponse
}
