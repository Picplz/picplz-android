package com.hm.picplz.ui.screen.detail_reservation

sealed interface DetailReservationIntent {
    data object NavigateToChat : DetailReservationIntent

    data object NavigateToHistory : DetailReservationIntent

    data object ConfirmReservation : DetailReservationIntent
}
