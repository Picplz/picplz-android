package com.hm.picplz.ui.screen.detail_photographer

import com.hm.picplz.ui.screen.detail_photographer.review.ReviewSortType

sealed interface DetailPhotographerIntent {
    data object NavigateToPrev : DetailPhotographerIntent

    data object SelectBooking : DetailPhotographerIntent

    data object ToggleFollow : DetailPhotographerIntent

    data object ToggleInfoExpanded : DetailPhotographerIntent

    data object ToggleBlock : DetailPhotographerIntent

    data object ToggleAreaExpanded : DetailPhotographerIntent

    data object ToggleMenuSheet : DetailPhotographerIntent

    data class SelectReviewSort(val sortType: ReviewSortType) : DetailPhotographerIntent

    data object ToggleSortSheet : DetailPhotographerIntent

    data class ShowFullScreenPhoto(val imageUri: String) : DetailPhotographerIntent

    data object DismissFullScreenPhoto : DetailPhotographerIntent

    data object ToggleReportSheet : DetailPhotographerIntent

    data object DismissPreviewActionDialog : DetailPhotographerIntent

    data class SwitchReview(val reviewIndex: Int) : DetailPhotographerIntent

    data object DismissToast : DetailPhotographerIntent
}
