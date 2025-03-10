package com.hm.picplz.ui.screen.detail_photographer

sealed class DetailPhotographerReviewIntent {
    data object NavigateToPrev : DetailPhotographerReviewIntent()
}