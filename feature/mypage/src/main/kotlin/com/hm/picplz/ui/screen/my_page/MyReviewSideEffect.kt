package com.hm.picplz.ui.screen.my_page

sealed interface MyReviewSideEffect {
    data object NavigateBack : MyReviewSideEffect
}
