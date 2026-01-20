package com.hm.picplz.ui.screen.detail_photographer

sealed interface DetailPhotographerIntent {
    data object NavigateToPrev : DetailPhotographerIntent
}
