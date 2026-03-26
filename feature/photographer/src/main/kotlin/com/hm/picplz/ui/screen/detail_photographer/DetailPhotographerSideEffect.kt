package com.hm.picplz.ui.screen.detail_photographer

sealed interface DetailPhotographerSideEffect {
    data object NavigateToPrev : DetailPhotographerSideEffect
}
