package com.hm.picplz.ui.screen.cancel_reservation

sealed interface CancelReservationSideEffect {
    data object NavigateToHistory : CancelReservationSideEffect

    data object NavigateToHome : CancelReservationSideEffect

    data object NavigateToPrev : CancelReservationSideEffect
}
