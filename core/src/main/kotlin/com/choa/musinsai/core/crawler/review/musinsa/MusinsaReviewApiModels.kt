package com.choa.musinsai.core.crawler.review.musinsa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaReviewApiResponse(
    val data: MusinsaReviewData = MusinsaReviewData(),
    val meta: MusinsaReviewMeta = MusinsaReviewMeta()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaReviewData(
    val list: List<MusinsaReview> = emptyList(),
    val total: Int = 0,
    val page: MusinsaReviewPagination? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaReviewPagination(
    val page: Int = 1,
    val totalPages: Int = 1,
    val startPage: Int = 1,
    val lastPage: Int = 1
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaReviewMeta(
    val result: String = "",
    val errorCode: String? = null,
    val message: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaReview(
    val no: Long = 0,
    val type: String = "",
    val typeName: String = "",
    val subType: String? = null,
    val content: String = "",
    val commentCount: Int = 0,
    val grade: String = "0",
    val goods: ReviewGoods = ReviewGoods(),
    val userImageFile: String? = null,
    val goodsOption: String = "",
    val commentReplyCount: Int = 0,
    val userStaffYn: String = "N",
    val images: List<ReviewImage>? = emptyList(),
    val likeCount: Int = 0,
    val userReactionType: String? = null,
    val createDate: String = "",
    val goodsThumbnailImageUrl: String? = null,
    val userId: String? = null,
    val encryptedUserId: String = "",
    val userProfileInfo: ReviewUserProfile = ReviewUserProfile(),
    val orderOptionNo: Long = 0,
    val channelSource: String = "",
    val channelSourceName: String = "",
    val channelActivityId: String = "",
    val relatedNo: Long = 0,
    val isFirstReview: Int = 0,
    val reviewProfileTypeEnum: String = "",
    val specialtyCodes: String? = null,
    val reviewerWeeklyRanking: Int? = null,
    val reviewerMonthlyRanking: Int? = null,
    val showUserProfile: Boolean = false
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewGoods(
    val goodsNo: Long = 0,
    val goodsSubNo: Long = 0,
    val goodsName: String = "",
    val goodsImageFile: String? = null,
    val goodsImageExtension: String? = null,
    val goodsOptionKindCode: String? = null,
    val brandName: String = "",
    val brandEnglishName: String? = null,
    val brand: String = "",
    val brandBestYn: String? = null,
    val brandConcatenation: String? = null,
    val goodsCreateDate: String? = null,
    val goodsImageIdx: Int? = null,
    val saleStatCode: String? = null,
    val saleStatLabel: String? = null,
    val goodsSex: Int? = null,
    val goodsSexClassification: String? = null,
    val showSoldOut: Boolean = false
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewUserProfile(
    val userNickName: String = "",
    val userLevel: Int = 0,
    val userOutYn: String? = null,
    val userStaffYn: String? = null,
    val reviewSex: String? = null,
    val userWeight: Int? = null,
    val userHeight: Int? = null,
    val userSkinInfo: String? = null,
    val skinType: String? = null,
    val skinTone: String? = null,
    val skinWorry: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewImage(
    val altText: String? = null,
    val imageUrl: String = ""
)
