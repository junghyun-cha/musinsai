package com.choa.musinsai.core.provider.crawler.user

data class UserProfileResponse(
    val profile: UserProfile,
    val benefits: UserBenefits,
    val menus: List<UserMenuItem>,
    val bottomSheets: List<BottomSheet>,
    val rollingBanners: List<RollingBanner>,
    val nudgeBanner: NudgeBanner?
)

data class UserProfile(
    val hashId: String,
    val level: Int,
    val nickName: String,
    val profileImageUrl: String?,
    val profileSettingUrl: String,
    val brandClubTitle: String?,
    val brandClubRankText: String?,
    val brandClubBadgeImageUrl: String?,
    val brandClubUrl: String?
)

data class UserBenefits(
    val points: BenefitItem?,
    val musinsaMoney: BenefitItem?,
    val coupons: BenefitItem?,
    val writableReviews: ReviewBenefit?
)

data class BenefitItem(
    val title: String,
    val amount: String,
    val linkUrl: String
)

data class ReviewBenefit(
    val title: String,
    val subTitle: String,
    val linkUrl: String
)

data class UserMenuItem(
    val type: String,
    val title: String,
    val subTitle: String?,
    val linkUrl: String?,
    val buttonName: String?,
    val existNewBadge: Boolean,
    val contents: List<BannerContent>? = null
)

data class BannerContent(
    val title: String,
    val subTitle1: String?,
    val linkUrl: String,
    val imageUrl: String?,
    val startDate: String?,
    val endDate: String?,
    val buttonName: String?
)

data class BottomSheet(
    val id: String,
    val title: String,
    val description: String,
    val descriptionArgs: List<String>,
    val buttons: List<BottomSheetButton>,
    val imageUrl: String?,
    val type: String,
    val serviceType: String,
    val repeatType: String
)

data class BottomSheetButton(
    val buttonName: String,
    val linkUrl: String
)

data class RollingBanner(
    val text: String,
    val args: List<String>?,
    val linkUrl: String
)

data class NudgeBanner(
    val contents: List<NudgeBannerContent>
)

data class NudgeBannerContent(
    val title: String,
    val serviceType: String,
    val subTitle: String,
    val linkUrl: String,
    val imageUrl: String,
    val titleColor: String,
    val subTitleColor: String,
    val iconTintColor: String,
    val backgroundColor: String
)
