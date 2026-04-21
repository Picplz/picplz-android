package com.hm.picplz.ui.screen.detail_reservation.model

import androidx.annotation.StringRes
import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.feature.reservation.R

enum class RefundCondition(
    val percent: Int,
    @StringRes val conditionResId: Int,
) {
    WITHIN_24_HOURS(
        percent = 100,
        conditionResId = R.string.refund_condition_within_24_hours,
    ),

    BEFORE_7_DAYS(
        percent = 100,
        conditionResId = R.string.refund_condition_before_7_days,
    ),

    BEFORE_3_DAYS(
        percent = 90,
        conditionResId = R.string.refund_condition_before_3_days,
    ),

    BEFORE_2_DAYS(
        percent = 70,
        conditionResId = R.string.refund_condition_before_2_days,
    ),

    BEFORE_1_DAY(
        percent = 50,
        conditionResId = R.string.refund_condition_before_1_day,
    ),

    SAME_DAY(
        percent = 0,
        conditionResId = R.string.refund_condition_same_day,
    ),
    ;

    fun isFullRefund() = this == WITHIN_24_HOURS || this == BEFORE_7_DAYS

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
         * @param currentMillis 현재 시간 (milliseconds)
         * @param shootingMillis 촬영 예정 시간 (milliseconds)
         * @param confirmedMillis 촬영 확정 시간 (milliseconds, nullable)
         */
        fun calculate(
            currentMillis: Long,
            shootingMillis: Long,
            confirmedMillis: Long?,
        ): RefundCondition {
            // 촬영 확정 후 24시간 이내 체크
            confirmedMillis?.let {
                val hoursSinceConfirmed = DateTimeUtil.hoursBetween(it, currentMillis)
                if (hoursSinceConfirmed < 24) {
                    return WITHIN_24_HOURS
                }
            }

            // 촬영일까지 남은 일수 계산
            val daysUntilShooting = DateTimeUtil.daysBetween(currentMillis, shootingMillis)

            return when {
                daysUntilShooting >= 7 -> BEFORE_7_DAYS
                daysUntilShooting >= 3 -> BEFORE_3_DAYS
                daysUntilShooting >= 2 -> BEFORE_2_DAYS
                daysUntilShooting >= 1 -> BEFORE_1_DAY
                else -> SAME_DAY
            }
        }
    }
}
