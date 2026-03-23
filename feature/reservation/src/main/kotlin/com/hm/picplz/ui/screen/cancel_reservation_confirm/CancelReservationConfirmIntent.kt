package com.hm.picplz.ui.screen.cancel_reservation_confirm

sealed interface CancelReservationConfirmIntent {
    data object NavigateToHistory : CancelReservationConfirmIntent

    data object NavigateToHome : CancelReservationConfirmIntent

    data object NavigateBack : CancelReservationConfirmIntent
}
