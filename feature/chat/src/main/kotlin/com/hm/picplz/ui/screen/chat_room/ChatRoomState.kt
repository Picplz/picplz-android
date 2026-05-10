package com.hm.picplz.ui.screen.chat_room

import com.hm.picplz.common.model.User
import com.hm.picplz.domain.model.ButtonActionType
import com.hm.picplz.domain.model.ChatMessage
import com.hm.picplz.domain.model.MessageButton
import com.hm.picplz.domain.model.MessageContent
import com.hm.picplz.domain.model.MessageDirection
import com.hm.picplz.domain.model.NotificationType

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

val dummyChatMessages =
    listOf(
        ChatMessage(
            id = 1,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
            content = MessageContent.Text("말풍선 어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다"),
            timestamp = System.currentTimeMillis() - 100000,
        ),
        ChatMessage(
            id = 2,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content = MessageContent.Text("말풍선 어쩌고 가로 최대 크기는 이만큼 입니다"),
            timestamp = System.currentTimeMillis() - 10000,
        ),
        ChatMessage(
            id = 3,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
            content =
                MessageContent.Image(
                    imageUris =
                        listOf(
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 4,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            content = MessageContent.Text("이거 사진 너무 잘나왔어요\n감사해요"),
            timestamp = System.currentTimeMillis() - 10000,
        ),
        ChatMessage(
            id = 5,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content =
                MessageContent.Notification(
                    title = "상품명",
                    subtitle = "긍정적인 안내",
                    content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
                    type = NotificationType.POSITIVE,
                    button =
                        MessageButton(
                            text = "확인",
                            actionType = ButtonActionType.CONFIRM_ORDER,
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 6,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content =
                MessageContent.Image(
                    imageUris =
                        listOf(
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                            "https://i.pinimg.com/736x/38/f4/0e/38f40e3944d8e5e63cc33594e2111ec7.jpg",
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 7,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content =
                MessageContent.DealConfirmation(
                    button =
                        MessageButton(
                            text = "거래 확정",
                            actionType = ButtonActionType.CONFIRM_ORDER,
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 8,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            content =
                MessageContent.DealConfirmation(
                    button =
                        MessageButton(
                            text = "거래 확정",
                            actionType = ButtonActionType.CONFIRM_ORDER,
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 9,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            content =
                MessageContent.ChangeTime(
                    newScheduledTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24,
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 10,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            content =
                MessageContent.Completion(
                    title = "상품명",
                    deliveryMethod = com.hm.picplz.domain.model.DeliveryType.EMAIL,
                    deliveryDeadline = System.currentTimeMillis() + 1000 * 60 * 60 * 24,
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 11,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content =
                MessageContent.Completion(
                    title = "상품명",
                    deliveryMethod = com.hm.picplz.domain.model.DeliveryType.EMAIL,
                    deliveryDeadline = System.currentTimeMillis() + 1000 * 60 * 60 * 24,
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 12,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content =
                MessageContent.Notification(
                    title = "상품명",
                    subtitle = "부정적인 안내",
                    content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
                    type = NotificationType.NEGATIVE,
                    button =
                        MessageButton(
                            text = "확인",
                            actionType = ButtonActionType.CONFIRM_ORDER,
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
        ChatMessage(
            id = 13,
            direction = MessageDirection.SENT,
            sender =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            receiver =
                User(
                    id = "2",
                    nickname = "유가영작가",
                    profileImageUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large",
                ),
            content =
                MessageContent.ChatSuggest(
                    suggestedChats =
                        listOf(
                            "촬영 소요 시간은 얼마나 걸리나요",
                            "촬영 장소를 추천해주실 수 있나요",
                            "현장에서 옵션 변경이 가능한가요?",
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
    )

val reservationCustomerName = "애니프사"
val reservationProductName = "자연스러운프사"
val reservationProfileUri = "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large"

val dummyReservationChatMessages =
    listOf(
        ChatMessage(
            id = 100,
            direction = MessageDirection.RECEIVED,
            sender =
                User(
                    id = "2",
                    nickname = reservationCustomerName,
                    profileImageUri = reservationProfileUri,
                ),
            receiver =
                User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
            content =
                MessageContent.Notification(
                    title = "[$reservationProductName] 상품",
                    subtitle = "예약 신청이 도착했어요",
                    content = "$reservationCustomerName 님으로부터 [$reservationProductName] 예약이 도착했습니다.",
                    type = NotificationType.POSITIVE,
                    button =
                        MessageButton(
                            text = "예약 정보 확인",
                            actionType = ButtonActionType.CONFIRM_ORDER,
                        ),
                ),
            timestamp = System.currentTimeMillis() - 1000,
        ),
    )

val dummySuggestedChat =
    listOf(
        "촬영 소요 시간은 얼마나 걸리나요",
        "촬영 장소를 추천해주실 수 있나요",
        "현장에서 옵션 변경이 가능한가요?",
    )
