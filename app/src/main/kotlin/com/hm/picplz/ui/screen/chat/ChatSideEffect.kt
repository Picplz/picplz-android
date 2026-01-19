package com.hm.picplz.ui.screen.chat

sealed interface ChatSideEffect {
    data class NavigateToChatRoom(val chatId: String) : ChatSideEffect
}