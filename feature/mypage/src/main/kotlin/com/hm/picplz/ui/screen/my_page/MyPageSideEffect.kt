package com.hm.picplz.ui.screen.my_page

sealed interface MyPageSideEffect {
    data object NavigateToModifyProfile : MyPageSideEffect

    data object NavigateToMyReviews : MyPageSideEffect

    data object NavigateToFollowedPhotographers : MyPageSideEffect

    data object NavigateToShootingHistory : MyPageSideEffect

    data object NavigateToSettings : MyPageSideEffect

    data class ShowToast(val message: String) : MyPageSideEffect
}
