package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePhotographerKeywordEditSideEffect {
    data class NavigateToPrev(
        val selectedKeywords: List<String>? = null,
    ) : MyPagePhotographerKeywordEditSideEffect
}
