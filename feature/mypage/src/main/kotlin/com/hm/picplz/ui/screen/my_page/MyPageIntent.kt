package com.hm.picplz.ui.screen.my_page

sealed interface MyPageIntent {
    data object NavigateToModifyProfile : MyPageIntent

    data object NavigateToShootingHistory : MyPageIntent

    data object NavigateToSettings : MyPageIntent

    data object NavigateToFollowedPhotographers : MyPageIntent

    data object NavigateToMyReviews : MyPageIntent

    data object NavigateToTerms : MyPageIntent

    data object SwitchToPhotographer : MyPageIntent

    data object NavigateToPhotographerSignUp : MyPageIntent

    data object DevToggleShootings : MyPageIntent
}
