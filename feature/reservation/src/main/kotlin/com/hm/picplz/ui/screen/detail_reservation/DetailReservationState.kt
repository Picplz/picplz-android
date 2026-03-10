package com.hm.picplz.ui.screen.detail_reservation

import com.hm.picplz.ui.screen.detail_reservation.model.RefundReason
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import java.time.LocalDateTime

data class DetailReservationState(
    val reservationStatus: ReservationStatus = ReservationStatus.WAITING_APPROVAL,
    val showCancelDialog: Boolean = false,
    val shootingDateTime: LocalDateTime? = null,
    val confirmedDateTime: LocalDateTime? = null,
    val refundReason: RefundReason? = null,
)
