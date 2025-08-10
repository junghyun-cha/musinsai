package com.choa.musinsai.core.provider.crawler.review

interface ReviewCrawler {
    fun search(request: ReviewSearchRequest): ReviewSearchResponse
}
