package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.chat.ChatIntent
import com.hm.picplz.ui.screen.chat.ChatSideEffect
import com.hm.picplz.ui.screen.chat.ChatState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(ChatState.idle())
    val state: StateFlow<ChatState> get() = _state

    private val _sideEffect = MutableSharedFlow<ChatSideEffect>()
    val sideEffect: SharedFlow<ChatSideEffect> get() = _sideEffect

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.NavigateToChatRoom -> {
                viewModelScope.launch {
                    _sideEffect.emit(ChatSideEffect.NavigateToChatRoom(chatId = intent.chatId))
                }
            }
            is ChatIntent.SetSelectedTab -> {
                _state.update {
                    it.copy(
                        selectedTab = intent.tabType,
                        selectedStatusTag = null
                    )
                }
            }
            is ChatIntent.SetStatusTags -> {
                _state.update {
                    it.copy(
                        selectedStatusTag = if (it.selectedStatusTag == intent.statusTag) null else intent.statusTag
                    )
                }
            }
            is ChatIntent.ToggleChatRoomMute -> {
                _state.update { current ->
                    val isCurrentlyMuted = current.mutedRoomIds.contains(intent.chatId)
                    current.copy(
                        mutedRoomIds = if (isCurrentlyMuted) {
                            current.mutedRoomIds - intent.chatId
                        } else {
                            current.mutedRoomIds + intent.chatId
                        }
                    )
                }
            }
        }
    }
}