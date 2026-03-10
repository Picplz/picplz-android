package com.hm.picplz.ui.screen.detail_reservation

import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

data class DetailReservationState(
    val reservationStatus: ReservationStatus = ReservationStatus.WAITING_APPROVAL,
    val showCancelDialog: Boolean = false,
)
