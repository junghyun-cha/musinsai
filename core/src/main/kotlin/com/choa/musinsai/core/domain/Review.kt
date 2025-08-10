package com.choa.musinsai.core.domain

import java.time.LocalDateTime

data class Review(
    val id: String,
    val productId: String,
    val userId: String? = null,
    val userName: String,
    val rating: Int,
    val content: String,
    val images: List<String> = emptyList(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val purchaseOption: String? = null,
    val userProfile: UserProfile? = null,
    val createdAt: LocalDateTime,
    val isVerifiedPurchase: Boolean = false,
    val platform: ShoppingPlatform
)

data class UserProfile(
    val height: Int? = null,
    val weight: Int? = null,
    val bodyType: String? = null,
    val skinType: String? = null,
    val age: Int? = null,
    val gender: Gender? = null
)

data class ReviewSummary(
    val productId: String,
    val totalCount: Int,
    val averageRating: Double,
    val ratingDistribution: Map<Int, Int>,
    val positiveKeywords: List<String> = emptyList(),
    val negativeKeywords: List<String> = emptyList(),
    val mostHelpfulReviews: List<Review> = emptyList()
)

data class ReviewSearchResult(
    val reviews: List<Review>,
    val totalCount: Int,
    val currentPage: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val summary: ReviewSummary? = null
)
