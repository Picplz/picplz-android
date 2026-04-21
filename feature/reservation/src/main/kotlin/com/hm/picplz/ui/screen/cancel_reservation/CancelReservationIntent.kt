package com.hm.picplz.ui.screen.cancel_reservation

sealed interface CancelReservationIntent {
    data class ToggleReason(val reason: CancelReason) : CancelReservationIntent

    data class UpdateDirectInput(val text: String) : CancelReservationIntent

    data class UpdateAgreement(val isChecked: Boolean) : CancelReservationIntent

    data object OnBackClick : CancelReservationIntent

    data object OnNextClick : CancelReservationIntent

    data object DismissCancelDialog : CancelReservationIntent

    data object ConfirmCancel : CancelReservationIntent

    data object ShowRefundPolicyDialog : CancelReservationIntent

    data object DismissRefundPolicyTooltip : CancelReservationIntent
}
