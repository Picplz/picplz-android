package com.hm.picplz.ui.screen.cancel_reservation

sealed interface CancelReservationIntent {
    data object NavigateToHistory : CancelReservationIntent

    data object NavigateToHome : CancelReservationIntent

    data object NavigateBack : CancelReservationIntent
}
