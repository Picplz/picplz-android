package com.hm.picplz.ui.screen.cancel_reservation_confirm

import com.hm.picplz.common.model.CancelConfirmType

data class CancelReservationConfirmState(
    val cancelType: CancelConfirmType = CancelConfirmType.WITHOUT_REFUND,
)
