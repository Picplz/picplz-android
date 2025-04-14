package com.hm.picplz.ui.screen.chat

import com.hm.picplz.ui.model.ChatStatus

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