package com.choa.musinsai.core.provider.crawler.review

/**
 * Review search request for Musinsa (and potentially other providers).
 * Mirrors the previous parameter list to keep behavior unchanged while
 * improving call-site readability.
 */
data class ReviewSearchRequest(
    val goodsNo: Long,
    val page: Int = 1,
    val pageSize: Int = 20,
    val sort: String = "date_desc",
    val selectedSimilarNo: Long? = null,
    val myFilter: Boolean? = null,
    val hasPhoto: Boolean? = null,
    val isExperience: Boolean? = null
)
