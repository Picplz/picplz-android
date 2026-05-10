package com.hm.picplz.ui.screen.photographer_detail_reservation

sealed interface PhotographerDetailReservationSideEffect {
    data object NavigateToPrev : PhotographerDetailReservationSideEffect

    data object NavigateToRejectReservationConfirm : PhotographerDetailReservationSideEffect

    data object NavigateToOrderDetail : PhotographerDetailReservationSideEffect
}
