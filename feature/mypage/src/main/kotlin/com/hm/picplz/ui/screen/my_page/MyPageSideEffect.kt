package com.hm.picplz.ui.screen.my_page

sealed interface MyPageSideEffect {
    data object NavigateToPhotographerSignUp : MyPageSideEffect

    data object NavigateToModifyProfile : MyPageSideEffect

    data object NavigateToPhotographerModifyProfile : MyPageSideEffect

    data class NavigateToPhotographerKeywordEdit(val photographerId: Int) : MyPageSideEffect

    data object NavigateToPhotographerEquipmentEdit : MyPageSideEffect

    data object NavigateToPackageEdit : MyPageSideEffect

    data object NavigateToMyReviews : MyPageSideEffect

    data object NavigateToFollowedPhotographers : MyPageSideEffect

    data object NavigateToShootingHistory : MyPageSideEffect

    data object NavigateToSettings : MyPageSideEffect

    data class NavigateToPhotographerPreview(val photographerId: Int) : MyPageSideEffect

    data class ShowToast(val messageResId: Int) : MyPageSideEffect
}
