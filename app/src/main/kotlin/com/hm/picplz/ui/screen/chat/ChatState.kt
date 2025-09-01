package com.hm.picplz.ui.screen.chat

import com.hm.picplz.ui.model.ChatRoomInfo
import com.hm.picplz.ui.model.ChatStatus
import com.hm.picplz.ui.model.Message

enum class ChatTabType {
    ONGOING, COMPLETED
}

data class ChatState (
    val selectedTab : ChatTabType = ChatTabType.ONGOING,
    val statusTags: List<ChatStatus> = ChatStatus.entries
) {
    companion object {
        fun idle(): ChatState {
            return ChatState()
        }
    }
    val currentStatusTags: List<ChatStatus>
        get() = when (selectedTab) {
            ChatTabType.ONGOING -> listOf(ChatStatus.PENDING, ChatStatus.CONFIRMED)
            ChatTabType.COMPLETED -> listOf(ChatStatus.REJECTED, ChatStatus.COMPLETED)
        }
}

val dummyChatRooms =
    listOf(
    ChatRoomInfo(
        id = "chat1",
        chatStatus = ChatStatus.PENDING,
        packageType = "프로필 Only.",
        lastMessage = Message(
            profileImageUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=John",
            nickname = "합정동 불주먹",
            message = "촬영 예약이 도착했습니다. 60분 이내에 답변 안 할 시 예약이 취소될 수 있습니다.",
            sentAt = System.currentTimeMillis() - (1000 * 60 * 3),
        ),
        unreadMessageCount = 1,
    ),
    ChatRoomInfo(
        id = "chat1",
        chatStatus = ChatStatus.CONFIRMED,
        packageType = "인스타 종합 패키지",
        lastMessage = Message(
            profileImageUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=John",
            nickname = "오늘저역방어",
            message = "내일 14시에 뵐게요~~",
            sentAt = System.currentTimeMillis() - (1000 * 60 * 48),
        )
    ),
    ChatRoomInfo(
        id = "chat1",
        chatStatus = ChatStatus.CONFIRMED,
        packageType = "인스타 종합 패키지",
        lastMessage = Message(
            profileImageUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=John",
            nickname = "하배고파 ",
            message = "주문서 쓰기 너무 기차나요ㅜ 좀만 기달",
            sentAt = System.currentTimeMillis() - (1000 * 60 * 60),
        ),
        unreadMessageCount = 3
    ),
)