package com.hm.picplz.ui.screen.photographer_chat_room

sealed interface PhotographerChatRoomSideEffect {
    data object NavigateToPrev : PhotographerChatRoomSideEffect
}
