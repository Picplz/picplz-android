package com.hm.picplz.ui.screen.cancel_reservation

sealed interface CancelReservationSideEffect {
    data object NavigateBack : CancelReservationSideEffect

    data object ShowCancelConfirmModal : CancelReservationSideEffect
}
