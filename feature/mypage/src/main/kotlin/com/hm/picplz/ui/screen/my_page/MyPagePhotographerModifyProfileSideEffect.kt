package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePhotographerModifyProfileSideEffect {
    data object NavigateBack : MyPagePhotographerModifyProfileSideEffect
}
