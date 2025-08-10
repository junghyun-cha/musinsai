package com.choa.musinsai.core.crawler.review

import com.choa.musinsai.core.domain.ReviewSearchResult

interface ReviewCrawler {
    fun getProductReviews(request: ReviewSearchRequest): ReviewSearchResult
}
