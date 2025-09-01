package com.hm.picplz.ui.screen.chat_room

sealed class ChatRoomIntent {
    data object NavigateToPrev: ChatRoomIntent()
}