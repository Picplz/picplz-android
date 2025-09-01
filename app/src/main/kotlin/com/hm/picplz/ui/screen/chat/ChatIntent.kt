package com.hm.picplz.ui.screen.chat

import com.hm.picplz.ui.model.ChatStatus

sealed class ChatIntent {
    data class NavigateToChatRoom(val chatId: String) : ChatIntent()
    data class SetSelectedTab(val tabType: ChatTabType) : ChatIntent()
    data class SetStatusTags(val statusTag: ChatStatus) : ChatIntent()
}