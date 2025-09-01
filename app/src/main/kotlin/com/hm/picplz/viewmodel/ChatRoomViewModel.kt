package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.chat_room.ChatRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ChatRoomState.idle())
    val state: StateFlow<ChatRoomState> = _state

//    private val _sideEffect = MutableSharedFlow<ChatRoomSideEffect>()
}