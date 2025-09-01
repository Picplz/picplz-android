package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.chat.ChatIntent
import com.hm.picplz.ui.screen.chat.ChatSideEffect
import com.hm.picplz.ui.screen.chat_room.ChatRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ChatRoomState.idle())
    val state: StateFlow<ChatRoomState> = _state

    private val _sideEffect = MutableSharedFlow<ChatSideEffect>()
    val sideEffect: SharedFlow<ChatSideEffect> get() = _sideEffect

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.NavigateToChatRoom -> {
                viewModelScope.launch {
                    _sideEffect.emit(ChatSideEffect.NavigateToChatRoom(chatId = intent.chatId))
                }
            }
        }
    }
}