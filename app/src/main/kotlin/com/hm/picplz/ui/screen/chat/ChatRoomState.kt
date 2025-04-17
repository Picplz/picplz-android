package com.hm.picplz.ui.screen.chat

enum class ReservationStep {
    PENDING,
    IN_PROGRESS,
    CONFIRMED,
}

data class ChatRoomState(
    val chatRoomId: Int = 0,
    val reservationStep: ReservationStep = ReservationStep.PENDING,
) {
    companion object {
        fun idle(): ChatRoomState {
            return ChatRoomState()
        }
    }
}