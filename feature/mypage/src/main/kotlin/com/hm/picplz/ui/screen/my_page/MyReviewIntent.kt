package com.hm.picplz.ui.screen.my_page

sealed interface MyReviewIntent {
    data object NavigateBack : MyReviewIntent

    data class ToggleReviewExpansion(val reviewId: Int) : MyReviewIntent

    data class RequestDelete(val reviewId: Int) : MyReviewIntent

    data object DismissDeleteDialog : MyReviewIntent

    data object ConfirmDelete : MyReviewIntent
}
