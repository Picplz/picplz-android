package com.hm.picplz.ui.screen.my_page

data class FollowedPhotographersState(
    val isLoading: Boolean = true,
    val isUnavailable: Boolean = false,
    val photographers: List<FollowedPhotographerItem> = emptyList(),
) {
    companion object {
        fun idle() = FollowedPhotographersState()
    }
}

data class FollowedPhotographerItem(
    val id: Int,
    val name: String,
    val profileImageUri: String,
    val workingAreas: List<String>,
    val keywords: List<String>,
    val isBookable: Boolean,
)
