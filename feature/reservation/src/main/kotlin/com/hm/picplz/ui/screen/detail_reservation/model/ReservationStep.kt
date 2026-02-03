package com.hm.picplz.ui.screen.detail_reservation.model

import androidx.annotation.StringRes
import com.hm.picplz.feature.reservation.R

enum class ReservationStep(
    @get:StringRes val nameResId: Int,
) {
    WAITING(R.string.reservation_step_waiting),
    IN_PROGRESS(R.string.reservation_step_in_progress),
    CONFIRMED(R.string.reservation_step_confirmed),
    ;

    companion object {
        fun ReservationStep.hasPassed(target: ReservationStep): Boolean = this.ordinal <= target.ordinal
    }
}
