package com.choa.musinsai.core.crawler.shop

/**
 * Standardized detail response models for shop detail endpoint
 */

data class ShopFloorInfo(
    val floor: String,
    val description: String
)

data class ShopDetail(
    val shopName: String,
    val shopType: String,
    val address: String,
    val phone: List<String> = emptyList(),
    val operationHours: String? = null,
    val paymentServices: List<String> = emptyList(),
    val floorInfo: List<ShopFloorInfo> = emptyList(),
    val eventPageURL: String? = null,
    val pcMapURL: String? = null,
    val backgroundImageURL: String? = null,
    val mmapURL: String? = null
)

data class ShopDetailResult(
    val detail: ShopDetail?
)
