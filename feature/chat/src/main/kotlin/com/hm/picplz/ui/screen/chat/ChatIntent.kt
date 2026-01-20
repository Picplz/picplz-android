package com.hm.picplz.ui.screen.chat

import com.hm.picplz.domain.model.ChatStatus

sealed interface ChatIntent {
    data class NavigateToChatRoom(val chatId: String) : ChatIntent

    data class SetSelectedTab(val tabType: ChatTabType) : ChatIntent

    data class SetStatusTags(val statusTag: ChatStatus) : ChatIntent

    data class ToggleChatRoomMute(val chatId: String) : ChatIntent
}
