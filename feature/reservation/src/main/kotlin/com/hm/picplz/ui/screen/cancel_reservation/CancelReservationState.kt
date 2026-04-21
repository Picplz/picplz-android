package com.hm.picplz.ui.screen.cancel_reservation

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CancelReservationState(
    val orderId: String = "",
    val selectedReasons: Set<CancelReason> = emptySet(),
    val directInputText: String = "",
    val currentPagerPage: CancelReservationPagerPage = CancelReservationPagerPage.REASON_INPUT,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // 환불 안내 관련
    val shootingDate: LocalDate = LocalDate.now().plusDays(4),
    val cancelDate: LocalDate = LocalDate.now(),
    val shootingDateFormatted: String = "",
    val cancelDateFormatted: String = "",
    val totalPrice: Int = 12900,
    val refundPrice: Int = 11610,
    val cancellationFee: Int = 1290,
    val isAgreementChecked: Boolean = false,
) {
    fun isNextButtonEnabled(): Boolean =
        when (currentPagerPage) {
            CancelReservationPagerPage.REASON_INPUT ->
                selectedReasons.isNotEmpty() && (
                    selectedReasons.any { it != CancelReason.DIRECT_INPUT } || // 직접 입력 이외의 이유가 있거나
                        directInputText.isNotBlank() // 직접 입력 텍스트가 있으면 OK
                )
            CancelReservationPagerPage.REFUND_GUIDE -> isAgreementChecked
        }

    companion object {
        fun idle(orderId: String): CancelReservationState {
            val dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
            val shootingDate = LocalDate.now().plusDays(4)
            val cancelDate = LocalDate.now()

            return CancelReservationState(
                orderId = orderId,
                shootingDate = shootingDate,
                cancelDate = cancelDate,
                shootingDateFormatted = shootingDate.format(dateFormatter),
                cancelDateFormatted = cancelDate.format(dateFormatter),
            )
        }
    }
}
