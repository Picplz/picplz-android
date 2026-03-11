package com.hm.picplz.ui.screen.detail_reservation

sealed interface DetailReservationSideEffect {
    data object NavigateToPrev : DetailReservationSideEffect
}
