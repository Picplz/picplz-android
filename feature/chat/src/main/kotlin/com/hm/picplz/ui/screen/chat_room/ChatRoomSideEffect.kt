package com.hm.picplz.ui.screen.chat_room

sealed interface ChatRoomSideEffect {
    data object NavigateToPrev : ChatRoomSideEffect
}