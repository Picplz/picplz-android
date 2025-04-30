package com.hm.picplz.ui.screen.chat

import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ButtonActionType
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.MessageButton
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.model.NotificationType

sealed interface ChatListItem {
    data class DateHeader(val date: Long) : ChatListItem
    data class MessageItem(val message: ChatMessage) : ChatListItem
}

enum class ReservationStep {
    NOT_STARTED,
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

val dummyChatMessages = listOf(
    ChatMessage(
        id = 1,
        direction = MessageDirection.SENT,
        sender = User(
            id = "1",
            nickname = "나",
            profileImageUri = null,
        ),
        receiver = User(
            id = "2",
            nickname = "유가영 작가",
            profileImageUri = null,
        ),
        content = MessageContent.Text("말풍선 어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다"),
        timestamp = System.currentTimeMillis() - 100000
    ),
    ChatMessage(
        id = 2,
        direction = MessageDirection.RECEIVED,
        sender = User(
            id = "2",
            nickname = "유가영 작가",
            profileImageUri = null,
        ),
        receiver = User(
            id = "1",
            nickname = "나",
            profileImageUri = null,
        ),
        content = MessageContent.Text("말풍선 어쩌고 가로 최대 크기는 이만큼 입니다"),
        timestamp = System.currentTimeMillis() - 10000
    ),
    ChatMessage(
        id = 3,
        direction = MessageDirection.SENT,
        sender = User(
            id = "1",
            nickname = "나",
            profileImageUri = null,
        ),
        receiver = User(
            id = "2",
            nickname = "유가영 작가",
            profileImageUri = null,
        ),
        content = MessageContent.Image(imageUrl = "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg"),
        timestamp = System.currentTimeMillis() - 1000
    ),
    ChatMessage(
        id = 4,
        direction = MessageDirection.RECEIVED,
        sender = User(
            id = "2",
            nickname = "유가영작가",
            profileImageUri = null,
        ),
        receiver = User(
            id = "1",
            nickname = "나",
            profileImageUri = null,
        ),
        content = MessageContent.Notification(
            title = "상품명",
            subtitle = "긍정적인 안내",
            content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
            type = NotificationType.POSITIVE,
            button = MessageButton(
                text = "확인",
                actionType = ButtonActionType.CONFIRM_ORDER
            ),
        ),
        timestamp = System.currentTimeMillis() - 1000,
    )
)