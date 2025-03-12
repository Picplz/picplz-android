package com.hm.picplz.ui.screen.detail_photographer

sealed class DetailPhotographerSideEffect {
    data object NavigateToPrev : DetailPhotographerSideEffect()
}