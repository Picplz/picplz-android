package com.hm.picplz.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class CancelConfirmType {
    WITH_REFUND, // 결제 이후 예약 취소 (환불 처리)
    WITHOUT_REFUND, // 결제 전 예약 취소 (환불 없음)
}
