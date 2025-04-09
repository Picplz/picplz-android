package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.chat.ChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ChatViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(ChatState.idle())
    val state: StateFlow<ChatState> get() = _state
}