package com.choa.musinsai.core.crawler.shop.musinsa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopApiResponse(
    val meta: MusinsaOfflineShopMeta = MusinsaOfflineShopMeta(),
    val data: MusinsaOfflineShopData = MusinsaOfflineShopData()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopMeta(
    val result: String? = null,
    val errorCode: String? = null,
    val message: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopData(
    val total: Int = 0,
    val list: List<MusinsaOfflineShopItem> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopItem(
    val shopNo: Long = 0,
    val shopType: String = "",
    val shopName: String = "",
    val address: String = ""
)

// Public result models (simple, decoupled from API schema if needed later)
data class OfflineShop(
    val shopNo: Long,
    val shopType: String,
    val shopName: String,
    val address: String
)

data class OfflineShopSearchResult(
    val total: Int,
    val shops: List<OfflineShop>
)
