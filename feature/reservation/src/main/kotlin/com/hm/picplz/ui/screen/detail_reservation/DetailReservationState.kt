package com.hm.picplz.ui.screen.detail_reservation

import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import java.time.LocalDateTime

data class DetailReservationState(
    val orderId: String = "",
    val reservationStatus: ReservationStatus = ReservationStatus.WAITING_APPROVAL,
    val showCancelDialog: Boolean = false,
    val shootingDateTime: LocalDateTime = LocalDateTime.now(),
    val confirmedDateTime: LocalDateTime = LocalDateTime.now(),
    val refundCondition: RefundCondition = RefundCondition.WITHIN_24_HOURS,
    val showRefundPolicyTooltip: Boolean = false,
)
