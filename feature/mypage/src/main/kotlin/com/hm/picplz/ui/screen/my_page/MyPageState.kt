package com.hm.picplz.ui.screen.my_page

data class MyPageState(
    val nickname: String = "",
    val profileImageUri: String = "",
    val hasPhotographerRole: Boolean = false,
    val ongoingShootings: List<OngoingShootingItem> = emptyList(),
    val photographerProfile: PhotographerProfile = PhotographerProfile(),
    val isLoading: Boolean = false,
) {
    companion object {
        fun idle() = MyPageState()
    }
}

data class PhotographerProfile(
    val displayName: String = "",
    val profileImageUri: String = "",
    val followerCount: Int = 0,
    val packageCount: Int = 0,
    val portfolioCount: Int = 0,
    val instagramId: String = "",
    val isInstagramRegistered: Boolean = false,
    val introduction: String = "",
    val regionSummary: String = "",
    val keywordSummary: String = "",
    val equipmentSummary: String = "",
    val hasPackages: Boolean = false,
    val packagePreview: PhotographerPackagePreview? = null,
    val satisfactionSummary: PhotographerSatisfactionSummary = PhotographerSatisfactionSummary(),
)

data class PhotographerPackagePreview(
    val imageResId: Int,
    val title: String,
    val price: Int,
    val meta: String,
    val description: String,
)

data class PhotographerSatisfactionSummary(
    val averageRating: String = "0.0",
    val reviewCount: Int = 0,
    val repeatBookingRate: Int = 0,
)

data class OngoingShootingItem(
    val photographerName: String,
    val photographerImageUri: String,
    val status: String,
    val packageName: String,
    val shootingDate: String,
    val shootingLocation: String,
)
