package com.choa.musinsai.core.crawler.review

interface ReviewCrawler {
    fun search(request: ReviewSearchRequest): ReviewSearchResponse
}
