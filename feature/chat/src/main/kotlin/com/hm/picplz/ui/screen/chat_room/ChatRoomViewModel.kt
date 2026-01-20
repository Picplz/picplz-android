package com.hm.picplz.ui.screen.chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _sideEffect = MutableSharedFlow<ChatRoomSideEffect>()
    val sideEffect: SharedFlow<ChatRoomSideEffect> = _sideEffect

    fun handleIntent(intent: ChatRoomIntent) {
        when (intent) {
            is ChatRoomIntent.NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(ChatRoomSideEffect.NavigateToPrev)
                }
            }
        }
    }
}