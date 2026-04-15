package com.hm.picplz.ui.screen.my_page

sealed interface FollowedPhotographersSideEffect {
    data object NavigateBack : FollowedPhotographersSideEffect

    data class NavigateToPhotographerDetail(val photographerId: Int) : FollowedPhotographersSideEffect
}
