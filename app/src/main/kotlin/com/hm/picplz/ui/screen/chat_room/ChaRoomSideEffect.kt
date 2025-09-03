package com.hm.picplz.ui.screen.chat_room

sealed class ChatRoomSideEffect {
    data object NavigateToPrev : ChatRoomSideEffect()
}