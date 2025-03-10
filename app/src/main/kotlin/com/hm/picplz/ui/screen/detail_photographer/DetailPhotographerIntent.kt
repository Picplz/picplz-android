package com.hm.picplz.ui.screen.detail_photographer

sealed class DetailPhotographerIntent {
    data object NavigateToPrev : DetailPhotographerIntent()
}