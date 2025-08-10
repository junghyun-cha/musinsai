package com.choa.musinsai.core.crawler.product

interface ProductCrawler {
    fun search(request: ProductSearchRequest): ProductSearchResponse
}
