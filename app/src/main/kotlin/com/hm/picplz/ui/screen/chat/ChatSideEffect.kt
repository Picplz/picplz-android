package com.hm.picplz.ui.screen.chat

sealed class ChatSideEffect {
    data class NavigateToChatRoom(val chatId: String): ChatSideEffect()
}