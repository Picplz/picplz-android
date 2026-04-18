package com.hm.picplz.ui.screen.cancel_reservation

data class CancelReservationState(
    val orderId: String = "",
    val selectedReasons: Set<CancelReason> = emptySet(),
    val directInputText: String = "",
    val currentPagerPage: CancelReservationPagerPage = CancelReservationPagerPage.REASON_INPUT,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    fun isNextButtonEnabled(): Boolean =
        selectedReasons.isNotEmpty() && (
            selectedReasons.any { it != CancelReason.DIRECT_INPUT } || // 직접 입력 이외의 이유가 있거나
                directInputText.isNotBlank() // 직접 입력 텍스트가 있으면 OK
        )

    companion object {
        fun idle(orderId: String): CancelReservationState = CancelReservationState(orderId = orderId)
    }
}
