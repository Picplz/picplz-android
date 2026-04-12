package com.hm.picplz.ui.screen.cancel_reservation

import com.hm.picplz.feature.reservation.R

enum class CancelReason(
    val stringRes: Int,
) {
    SCHEDULE(R.string.cancel_reason_option_schedule),
    PRODUCT(R.string.cancel_reason_option_product),
    PHOTOGRAPHER_LATE(R.string.cancel_reason_option_photographer_late),
    LOCATION(R.string.cancel_reason_option_location),
    OTHER(R.string.cancel_reason_option_other),
    MIND(R.string.cancel_reason_option_mind),
    DIRECT_INPUT(R.string.cancel_reason_option_direct_input),
}
