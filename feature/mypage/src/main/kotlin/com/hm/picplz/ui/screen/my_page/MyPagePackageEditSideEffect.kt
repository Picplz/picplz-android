package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePackageEditSideEffect {
    data object NavigateBack : MyPagePackageEditSideEffect

    data object LaunchImagePicker : MyPagePackageEditSideEffect

    data class ShowToast(val messageResId: Int) : MyPagePackageEditSideEffect
}
