package com.hm.picplz.ui.screen.chat_room

sealed interface ChatRoomIntent {
    data object NavigateToPrev : ChatRoomIntent
}
