package com.hm.picplz.ui.screen.detail_photographer

import com.hm.picplz.ui.screen.detail_photographer.review.ReviewSortType

sealed interface DetailPhotographerIntent {
    data object NavigateToPrev : DetailPhotographerIntent

    data object ToggleFollow : DetailPhotographerIntent

    data object ToggleInfoExpanded : DetailPhotographerIntent

    data object ToggleBlock : DetailPhotographerIntent

    data class SelectReviewSort(val sortType: ReviewSortType) : DetailPhotographerIntent

    data object ToggleSortSheet : DetailPhotographerIntent
}
