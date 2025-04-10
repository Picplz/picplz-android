package com.hm.picplz.ui.screen.chat

enum class ChatTabType {
    ONGOING, COMPLETED
}

data class ChatState (
    val selectedTab : ChatTabType = ChatTabType.ONGOING,
    val statusTags : List<String> = listOf("예약 대기", "예약 확정", "촬영 거절", "촬영 완료")
) {
    companion object {
        fun idle(): ChatState {
            return ChatState()
        }
    }
    val currentStatusTags: List<String>
        get() = when (selectedTab) {
            ChatTabType.ONGOING -> statusTags.filter { it == "예약 대기" || it == "예약 확정" }
            ChatTabType.COMPLETED -> statusTags.filter { it == "촬영 거절" || it == "촬영 완료" }
        }
}