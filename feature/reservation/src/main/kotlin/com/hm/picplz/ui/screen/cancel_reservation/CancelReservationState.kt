package com.hm.picplz.ui.screen.cancel_reservation

data class CancelReservationState(
    val orderId: String = "",
    val selectedReasons: Set<CancelReason> = emptySet(),
    val directInputText: String = "",
    val currentPagerPage: CancelReservationPagerPage = CancelReservationPagerPage.REASON_INPUT,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    companion object {
        fun idle(): CancelReservationState = CancelReservationState()
    }
}
