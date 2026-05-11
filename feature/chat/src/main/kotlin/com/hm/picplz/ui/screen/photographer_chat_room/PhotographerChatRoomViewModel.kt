package com.hm.picplz.ui.screen.photographer_chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.chat_room.dummyReservationChatMessages
import com.hm.picplz.ui.screen.chat_room.reservationCustomerName
import com.hm.picplz.ui.screen.chat_room.reservationProductName
import com.hm.picplz.ui.screen.chat_room.reservationProfileUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotographerChatRoomViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state =
            MutableStateFlow(
                PhotographerChatRoomState
                    .idle()
                    .copy(
                        chatMessages = dummyReservationChatMessages,
                        customerName = reservationCustomerName,
                        customerImageUrl = reservationProfileUri,
                        productName = reservationProductName,
                    ),
            )
        val state: StateFlow<PhotographerChatRoomState> = _state

        private val _sideEffect = MutableSharedFlow<PhotographerChatRoomSideEffect>()
        val sideEffect: SharedFlow<PhotographerChatRoomSideEffect> = _sideEffect

        fun handleIntent(intent: PhotographerChatRoomIntent) {
            when (intent) {
                is PhotographerChatRoomIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.emit(PhotographerChatRoomSideEffect.NavigateToPrev)
                    }
                }

                PhotographerChatRoomIntent.ClickReservationDetail -> {
                    viewModelScope.launch {
                        _sideEffect.emit(PhotographerChatRoomSideEffect.NavigateToPhotographerDetailReservation)
                    }
                }
            }
        }
    }
