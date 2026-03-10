package com.hm.picplz.ui.screen.detail_reservation.model

import androidx.annotation.StringRes
import com.hm.picplz.feature.reservation.R
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

sealed class RefundCondition(
    val percent: Int,
    @StringRes val conditionResId: Int,
) {
    data object Within24Hours : RefundCondition(
        percent = 100,
        conditionResId = R.string.refund_condition_within_24_hours,
    )

    data object Before7Days : RefundCondition(
        percent = 100,
        conditionResId = R.string.refund_condition_before_7_days,
    )

    data object Before3Days : RefundCondition(
        percent = 90,
        conditionResId = R.string.refund_condition_before_3_days,
    )

    data object Before2Days : RefundCondition(
        percent = 70,
        conditionResId = R.string.refund_condition_before_2_days,
    )

    data object Before1Day : RefundCondition(
        percent = 50,
        conditionResId = R.string.refund_condition_before_1_day,
    )

    data object SameDay : RefundCondition(
        percent = 0,
        conditionResId = R.string.refund_condition_same_day,
    )

    fun isFullRefund() = this == Within24Hours || this == Before7Days

    companion object {
        /**
         * 환불 규정을 계산합니다.
         *
         * 환불 규정:
         * - 촬영 확정 후 24시간 이내: 100%
         * - 촬영일 기준 7일 전까지: 100%
         * - 촬영일 기준 3일 전까지: 90%
         * - 촬영일 기준 2일 전까지: 70%
         * - 촬영일 기준 1일 전까지: 50%
         * - 당일 취소: 0% (환불 불가)
         */
        fun calculate(
            currentDateTime: LocalDateTime,
            shootingDateTime: LocalDateTime,
            confirmedDateTime: LocalDateTime?,
        ): RefundCondition {
            // 촬영 확정 후 24시간 이내 체크
            confirmedDateTime?.let {
                val hoursSinceConfirmed = ChronoUnit.HOURS.between(it, currentDateTime)
                if (hoursSinceConfirmed < 24) {
                    return Within24Hours
                }
            }

            // 촬영일까지 남은 일수 계산
            val daysUntilShooting =
                ChronoUnit.DAYS.between(
                    currentDateTime.toLocalDate(),
                    shootingDateTime.toLocalDate(),
                )

            return when {
                daysUntilShooting >= 7 -> Before7Days
                daysUntilShooting >= 3 -> Before3Days
                daysUntilShooting >= 2 -> Before2Days
                daysUntilShooting >= 1 -> Before1Day
                else -> SameDay
            }
        }
    }
}
