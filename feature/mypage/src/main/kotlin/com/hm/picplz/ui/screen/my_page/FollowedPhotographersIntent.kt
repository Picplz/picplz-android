package com.hm.picplz.ui.screen.my_page

sealed interface FollowedPhotographersIntent {
    data object NavigateBack : FollowedPhotographersIntent

    data class SelectPhotographer(val photographerId: Int) : FollowedPhotographersIntent
}
