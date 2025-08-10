package com.choa.musinsai.core.provider.crawler.product

interface ProductCrawler {
    fun search(request: ProductSearchRequest): ProductSearchResponse
}
