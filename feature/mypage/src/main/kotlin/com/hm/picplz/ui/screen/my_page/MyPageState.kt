package com.hm.picplz.ui.screen.my_page

data class MyPageState(
    val nickname: String = "",
    val profileImageUri: String = "",
    val hasPhotographerRole: Boolean = false,
    val ongoingShootings: List<OngoingShootingItem> = emptyList(),
    val isLoading: Boolean = false,
) {
    companion object {
        fun idle() = MyPageState()
    }
}

data class OngoingShootingItem(
    val photographerName: String,
    val photographerImageUri: String,
    val status: String,
    val packageName: String,
    val shootingDate: String,
    val shootingLocation: String,
)
