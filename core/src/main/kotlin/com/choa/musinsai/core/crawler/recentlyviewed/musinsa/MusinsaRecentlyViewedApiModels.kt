package com.choa.musinsai.core.crawler.recentlyviewed.musinsa

import com.fasterxml.jackson.annotation.JsonProperty

data class MusinsaRecentlyViewedBrandApiResponse(
    @JsonProperty("cd") val code: Int,
    @JsonProperty("msg") val message: String,
    @JsonProperty("data") val data: MusinsaRecentlyViewedBrandData
)

data class MusinsaRecentlyViewedBrandData(
    @JsonProperty("total") val total: String,
    @JsonProperty("brand_list") val brandList: List<MusinsaRecentlyViewedBrand>
)

data class MusinsaRecentlyViewedBrand(
    @JsonProperty("brand") val brand: String,
    @JsonProperty("brand_nm") val brandNm: String,
    @JsonProperty("brand_nm_eng") val brandNmEng: String,
    @JsonProperty("brand_img") val brandImg: String,
    @JsonProperty("link") val link: String,
    @JsonProperty("sex") val sex: String
)