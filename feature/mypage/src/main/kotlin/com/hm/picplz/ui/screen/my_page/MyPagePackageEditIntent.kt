package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePackageEditIntent {
    data object NavigateBack : MyPagePackageEditIntent
}
