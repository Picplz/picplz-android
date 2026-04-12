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
        selectedReasons.isNotEmpty() &&
            (!selectedReasons.contains(CancelReason.DIRECT_INPUT) || directInputText.isNotBlank())

    companion object {
        fun idle(orderId: String): CancelReservationState = CancelReservationState(orderId = orderId)
    }
}
