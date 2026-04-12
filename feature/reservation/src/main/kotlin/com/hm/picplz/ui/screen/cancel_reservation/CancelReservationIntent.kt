package com.hm.picplz.ui.screen.cancel_reservation

sealed interface CancelReservationIntent {
    data class ToggleReason(val reason: CancelReason) : CancelReservationIntent

    data class UpdateDirectInput(val text: String) : CancelReservationIntent

    data class UpdatePagerPage(val page: CancelReservationPagerPage) : CancelReservationIntent

    data object OnBackClick : CancelReservationIntent

    data object OnNextClick : CancelReservationIntent
}
