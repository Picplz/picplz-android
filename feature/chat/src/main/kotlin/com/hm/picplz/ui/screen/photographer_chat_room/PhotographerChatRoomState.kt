package com.hm.picplz.ui.screen.photographer_chat_room

import com.hm.picplz.domain.model.ChatMessage
import com.hm.picplz.ui.screen.chat_room.ReservationStep

data class PhotographerChatRoomState(
    val chatRoomId: Int = 0,
    val reservationStep: ReservationStep = ReservationStep.PENDING,
    val chatMessages: List<ChatMessage> = emptyList(),
) {
    companion object {
        fun idle(): PhotographerChatRoomState {
            return PhotographerChatRoomState()
        }
    }
}
