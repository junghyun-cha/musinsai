package com.choa.musinsai.core.crawler.product

data class ProductSearchRequest(
    val keyword: String,
    val gender: String = "A", // A: 전체, M: 남성, F: 여성
    val sortCode: String = "POPULAR", // POPULAR, SALE, PRICE_LOW, PRICE_HIGH, NEW
    val page: Int = 1,
    val size: Int = 60,
)
