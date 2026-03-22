package com.hm.picplz.ui.screen.cancel_reservation_confirm

sealed interface CancelReservationConfirmSideEffect {
    data object NavigateToHistory : CancelReservationConfirmSideEffect

    data object NavigateToHome : CancelReservationConfirmSideEffect

    data object NavigateToPrev : CancelReservationConfirmSideEffect
}
