package com.hm.picplz.ui.screen.detail_reservation.model

import androidx.annotation.StringRes
import com.hm.picplz.feature.reservation.R

enum class ReservationStatus(
    val step: ReservationStep,
    @get:StringRes val titleResId: Int,
    @get:StringRes val descriptionResId: Int,
) {
    WAITING_APPROVAL(
        step = ReservationStep.WAITING,
        titleResId = R.string.reservation_status_title_waiting_approval,
        descriptionResId = R.string.reservation_status_description_waiting_approval,
    ),
    WAITING_PAYMENT(
        step = ReservationStep.WAITING,
        titleResId = R.string.reservation_status_title_waiting_payment,
        descriptionResId = R.string.reservation_status_description_waiting_payment,
    ),
    RESERVED(
        step = ReservationStep.IN_PROGRESS,
        titleResId = R.string.reservation_status_title_reserved,
        descriptionResId = R.string.reservation_status_description_reserved,
    ),
    COMPLETED(
        step = ReservationStep.CONFIRMED,
        titleResId = R.string.reservation_status_title_completed,
        descriptionResId = R.string.reservation_status_description_completed,
    ),
    ;

    companion object {
        fun ReservationStatus.showCancelButton(): Boolean = this != COMPLETED
    }
}
