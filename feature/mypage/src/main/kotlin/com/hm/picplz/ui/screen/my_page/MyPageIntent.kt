package com.hm.picplz.ui.screen.my_page

sealed interface MyPageIntent {
    data object NavigateToModifyProfile : MyPageIntent

    data object NavigateToShootingHistory : MyPageIntent

    data object NavigateToSettings : MyPageIntent

    data object NavigateToFollowedPhotographers : MyPageIntent

    data object NavigateToMyReviews : MyPageIntent

    data object NavigateToTerms : MyPageIntent

    data object ToggleUserMode : MyPageIntent

    data object NavigateToPhotographerSignUp : MyPageIntent

    data object NavigateToPhotographerPreview : MyPageIntent

    data object NavigateToPhotographerRegionEdit : MyPageIntent

    data object NavigateToPhotographerKeywordEdit : MyPageIntent

    data object NavigateToPhotographerEquipmentEdit : MyPageIntent

    data object NavigateToSettlement : MyPageIntent

    data object NavigateToPackageEdit : MyPageIntent

    data object NavigateToPortfolioEdit : MyPageIntent

    data class ApplyDevPhotographerPreview(
        val hasShootings: Boolean,
        val hasPackagePreview: Boolean,
    ) : MyPageIntent
}
