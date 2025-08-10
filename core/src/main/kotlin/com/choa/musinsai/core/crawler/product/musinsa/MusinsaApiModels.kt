package com.choa.musinsai.core.crawler.product.musinsa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaApiResponse(
    val data: MusinsaData = MusinsaData()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaData(
    val list: List<MusinsaProduct> = emptyList(),
    val pagination: MusinsaPagination = MusinsaPagination()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaProduct(
    @JsonProperty("goodsNo")
    val goodsNo: Long = 0,

    @JsonProperty("goodsName")
    val goodsName: String = "",

    @JsonProperty("brandName")
    val brandName: String = "",

    @JsonProperty("price")
    val price: Long = 0,

    @JsonProperty("normalPrice")
    val normalPrice: Long = 0,

    @JsonProperty("saleRate")
    val saleRate: Int = 0,

    @JsonProperty("goodsLinkUrl")
    val goodsLinkUrl: String = "",

    @JsonProperty("goodsImageUrl")
    val goodsImageUrl: String? = null,

    @JsonProperty("isSoldOut")
    val isSoldOut: Boolean = false,

    @JsonProperty("reviewScore")
    val reviewScore: Double = 0.0,

    @JsonProperty("reviewCount")
    val reviewCount: Int = 0,

    @JsonProperty("displayGenderText")
    val displayGenderText: String? = null,

    @JsonProperty("tags")
    val tags: List<String> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaPagination(
    @JsonProperty("totalCount")
    val totalCount: Int = 0,

    @JsonProperty("pageNumber")
    val pageNumber: Int = 1,

    @JsonProperty("pageSize")
    val pageSize: Int = 60
)
