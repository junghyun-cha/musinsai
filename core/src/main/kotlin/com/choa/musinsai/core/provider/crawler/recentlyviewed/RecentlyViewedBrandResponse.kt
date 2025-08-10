package com.choa.musinsai.core.provider.crawler.recentlyviewed

data class RecentlyViewedBrandResponse(
    val total: String,
    val brandList: List<RecentlyViewedBrand>
)

data class RecentlyViewedBrand(
    val brand: String,
    val brandName: String,
    val brandNameEng: String,
    val brandImage: String,
    val link: String,
    val sex: String
)
