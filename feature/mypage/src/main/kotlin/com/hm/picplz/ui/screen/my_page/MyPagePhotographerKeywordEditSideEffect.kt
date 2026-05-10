package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePhotographerKeywordEditSideEffect {
    data class NavigateToPrev(
        val keywordSummary: String? = null,
    ) : MyPagePhotographerKeywordEditSideEffect
}
