package com.choa.musinsai.core.provider.crawler.shop.musinsa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// List API models
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

// Detail API models
@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopDetailApiResponse(
    val meta: MusinsaOfflineShopMeta = MusinsaOfflineShopMeta(),
    val data: MusinsaOfflineShopDetailData = MusinsaOfflineShopDetailData()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopDetailData(
    val detail: MusinsaOfflineShopDetail = MusinsaOfflineShopDetail()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopDetail(
    val shopName: String = "",
    val shopType: String = "",
    val address: String = "",
    val phone: List<String> = emptyList(),
    val operationHours: String? = null,
    val paymentServices: List<String> = emptyList(),
    val floorInfo: List<MusinsaOfflineShopFloorInfo> = emptyList(),
    val eventPageURL: String? = null,
    val pcMapURL: String? = null,
    val backgroundImageURL: String? = null,
    val mmapURL: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaOfflineShopFloorInfo(
    val floor: String = "",
    val description: String = ""
)
