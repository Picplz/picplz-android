package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePackageEditSideEffect {
    data object NavigateBack : MyPagePackageEditSideEffect
}
