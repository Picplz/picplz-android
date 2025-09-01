package com.hm.picplz.ui.screen.chat

sealed class ChatIntent {
    data class NavigateToChatRoom(val chatId: String) : ChatIntent()
    data class SetSelectedTab(val tabType: ChatTabType) : ChatIntent()
}