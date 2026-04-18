package com.hm.picplz.ui.screen.cancel_reservation

/**
 * 취소 예약 플로우의 페이지.
 *
 * 주의: 선언 순서 = 페이지 순서입니다.
 * 순서를 변경하면 HorizontalPager의 페이지 순서도 바뀝니다.
 */
enum class CancelReservationPagerPage {
    REASON_INPUT,
    REFUND_GUIDE,
    ;

    companion object {
        val size: Int get() = entries.size

        fun fromIndex(index: Int): CancelReservationPagerPage = entries.getOrNull(index) ?: REASON_INPUT
    }
}
