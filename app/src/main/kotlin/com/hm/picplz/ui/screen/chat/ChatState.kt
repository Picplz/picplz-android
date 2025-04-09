package com.hm.picplz.ui.screen.chat

enum class ChatTabType {
    ONGOING, COMPLETED
}

data class ChatState (
    val selectedTab : ChatTabType = ChatTabType.ONGOING
) {
    companion object {
        fun idle(): ChatState {
            return ChatState()
        }
    }
}