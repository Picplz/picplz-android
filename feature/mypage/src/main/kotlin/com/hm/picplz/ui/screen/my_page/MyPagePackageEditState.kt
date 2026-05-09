package com.hm.picplz.ui.screen.my_page

data class MyPagePackageEditState(
    val isSaveEnabled: Boolean = false,
) {
    companion object {
        fun idle() = MyPagePackageEditState()
    }
}
