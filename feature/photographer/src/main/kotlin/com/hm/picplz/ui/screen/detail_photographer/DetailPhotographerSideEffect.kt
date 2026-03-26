package com.hm.picplz.ui.screen.detail_photographer

sealed interface DetailPhotographerSideEffect {
    data object NavigateToPrev : DetailPhotographerSideEffect

    data class ShowBlockedToast(val name: String) : DetailPhotographerSideEffect
}
