package com.hm.picplz.ui.screen.photographer_chat_room

sealed interface PhotographerChatRoomIntent {
    data object NavigateToPrev : PhotographerChatRoomIntent
}
