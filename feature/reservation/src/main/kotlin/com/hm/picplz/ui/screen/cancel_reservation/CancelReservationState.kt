package com.hm.picplz.ui.screen.cancel_reservation

import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

data class CancelReservationState(
    val orderId: String = "",
    val selectedReasons: Set<CancelReason> = emptySet(),
    val directInputText: String = "",
    val currentPagerPage: CancelReservationPagerPage = CancelReservationPagerPage.REASON_INPUT,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // 환불 안내 관련
    val shootingDateMillis: Long = System.currentTimeMillis(),
    val cancelDateMillis: Long = System.currentTimeMillis(),
    val shootingDateFormatted: String = "",
    val cancelDateFormatted: String = "",
    val totalPrice: Int = 12900,
    val refundPrice: Int = 11610,
    val cancellationFee: Int = 1290,
    val isAgreementChecked: Boolean = false,
    val reservationStatus: ReservationStatus = ReservationStatus.RESERVED,
    val showCancelDialog: Boolean = false,
    val refundCondition: RefundCondition = RefundCondition.WITHIN_24_HOURS,
    val showRefundPolicyTooltip: Boolean = false,
) {
    fun isNextButtonEnabled(): Boolean =
        when (currentPagerPage) {
            CancelReservationPagerPage.REASON_INPUT -> {
                selectedReasons.isNotEmpty() && (
                    selectedReasons.any { it != CancelReason.DIRECT_INPUT } || // 직접 입력 이외의 이유가 있거나
                        directInputText.isNotBlank() // 직접 입력 텍스트가 있으면 OK
                )
            }

            CancelReservationPagerPage.REFUND_GUIDE -> isAgreementChecked
        }

    companion object {
        fun idle(orderId: String): CancelReservationState {
            val currentTimeMillis = System.currentTimeMillis()
            val shootingDateMillis = DateTimeUtil.plusDays(currentTimeMillis, 4)
            val cancelDateMillis = currentTimeMillis

            return CancelReservationState(
                orderId = orderId,
                shootingDateMillis = shootingDateMillis,
                cancelDateMillis = cancelDateMillis,
                shootingDateFormatted = DateTimeUtil.formatDate(shootingDateMillis, "yy.MM.dd"),
                cancelDateFormatted = DateTimeUtil.formatDate(cancelDateMillis, "yy.MM.dd"),
            )
        }
    }
}
