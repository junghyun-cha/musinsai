package com.choa.musinsai.core.crawler.product

data class ProductSearchRequest(
    val keyword: String,
    val gender: String = "A", // A: 전체, M: 남성, F: 여성
    val sortCode: String = "POPULAR", // POPULAR, SALE, PRICE_LOW, PRICE_HIGH, NEW
    val page: Int = 1,
    val size: Int = 60,
    val cookieString: String? = null // 인증이 필요한 경우 쿠키 문자열
)
