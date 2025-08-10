package com.choa.musinsai.core.provider.crawler.user.musinsa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaMyMenuResponse(
    val meta: MusinsaMeta = MusinsaMeta(),
    val data: MusinsaMyMenuData = MusinsaMyMenuData()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaMeta(
    val errorCode: String = "",
    val result: String = "",
    val message: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaMyMenuData(
    val profile: MusinsaProfile? = null,
    val shortcut: MusinsaShortcut? = null,
    val menus: List<MusinsaMenu> = emptyList(),
    val bottomSheets: List<MusinsaBottomSheet> = emptyList(),
    val rollingBanners: List<MusinsaRollingBanner> = emptyList(),
    val nudgeBanner: MusinsaNudgeBanner? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaProfile(
    val documentLocation: String? = null,
    val pageTitle: String? = null,
    val pageId: String? = null,
    val hashId: String = "",
    val level: Int = 0,
    val profileSettingUrl: String = "",
    val profileImageUrl: String? = null,
    val nickName: String = "",
    val snapUrl: String? = null,
    val brandClubBrandId: String? = null,
    val brandClubTitle: String? = null,
    val brandClubRankText: String? = null,
    val brandClubBadgeImageUrl: String? = null,
    val brandClubUrl: String? = null,
    val sectionName: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaShortcut(
    val horizontal: List<MusinsaShortcutItem> = emptyList(),
    val vertical: MusinsaShortcutItem? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaShortcutItem(
    val item: String = "",
    val title: String = "",
    val amount: String? = null,
    val subTitle: String? = null,
    val linkUrl: String = "",
    val sectionName: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaMenu(
    val type: String = "",
    val title: String = "",
    val subTitle: String? = null,
    val linkUrl: String? = null,
    val buttonName: String? = null,
    val existNewBadge: Boolean = false,
    val existSubTitleCount: Boolean = false,
    val subTitleCountUrl: String? = null,
    val sectionName: String? = null,
    val contents: List<MusinsaBannerContent>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaBannerContent(
    val title: String = "",
    val subTitle1: String? = null,
    val linkUrl: String = "",
    val imageUrl: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val buttonName: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaBottomSheet(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val descriptionArgs: List<String> = emptyList(),
    val buttonWidthRate: String? = null,
    val buttons: List<MusinsaBottomSheetButton> = emptyList(),
    val imageUrl: String? = null,
    val type: String = "",
    val serviceType: String = "",
    val repeatType: String = "",
    val sectionName: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaBottomSheetButton(
    val buttonName: String = "",
    val linkUrl: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaRollingBanner(
    val text: String = "",
    val args: List<String>? = null,
    val linkUrl: String = "",
    val sectionName: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaNudgeBanner(
    val contents: List<MusinsaNudgeBannerContent> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MusinsaNudgeBannerContent(
    val title: String = "",
    val serviceType: String = "",
    val subTitle: String = "",
    val linkUrl: String = "",
    val imageUrl: String = "",
    val titleColor: String = "",
    val subTitleColor: String = "",
    val iconTintColor: String = "",
    val backgroundColor: String = "",
    val sectionName: String? = null
)
