package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.my_page.shootingHistoryCard.ShootingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShootingHistoryViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val forceEmpty: Boolean = savedStateHandle.get<Boolean>("forceEmpty") ?: false

        private val _state = MutableStateFlow(ShootingHistoryState.idle())
        val state: StateFlow<ShootingHistoryState> get() = _state

        private val _sideEffect = Channel<ShootingHistorySideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            loadHistories()
        }

        private fun loadHistories() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                // TODO: Replace with actual API call
                _state.update {
                    it.copy(
                        isLoading = false,
                        shootingHistories = if (forceEmpty) emptyList() else DEV_MOCK_HISTORIES,
                    )
                }
            }
        }

        fun handleIntent(intent: ShootingHistoryIntent) {
            when (intent) {
                is ShootingHistoryIntent.NavigateBack -> {
                    sendSideEffect(ShootingHistorySideEffect.NavigateBack)
                }
                is ShootingHistoryIntent.NavigateToChat -> {
                    val item = findItem(intent.historyId)
                    if (item?.hasChatRoom == true) {
                        sendSideEffect(
                            ShootingHistorySideEffect.ShowToast("채팅 기능은 준비 중입니다."),
                        )
                    } else {
                        sendSideEffect(
                            ShootingHistorySideEffect.ShowToast("채팅방이 없습니다."),
                        )
                    }
                }
                is ShootingHistoryIntent.WriteReview -> {
                    sendSideEffect(
                        ShootingHistorySideEffect.ShowToast("리뷰 기능은 준비 중입니다."),
                    )
                }
                is ShootingHistoryIntent.ViewOrderDetail -> {
                    sendSideEffect(
                        ShootingHistorySideEffect.NavigateToOrderDetail(intent.historyId),
                    )
                }
                is ShootingHistoryIntent.DeleteHistory -> {
                    _state.update { current ->
                        current.copy(
                            shootingHistories =
                                current.shootingHistories.filter { it.id != intent.historyId },
                        )
                    }
                }
            }
        }

        private fun findItem(historyId: String): ShootingHistoryItem? =
            _state.value.shootingHistories.find { it.id == historyId }

        private fun sendSideEffect(effect: ShootingHistorySideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }

        companion object {
            private val DEV_MOCK_HISTORIES =
                listOf(
                    ShootingHistoryItem(
                        id = "1",
                        photographerName = "합정동작가",
                        photographerImageUri = "",
                        productName = "남친생기는 프사",
                        price = 12000,
                        status = ShootingStatus.CANCELLED,
                        paymentDate = "2025.03.01",
                        shootingDate = "25.03.24 | 오후2:30",
                        shootingLocation = "종로구 효자로 33 어디어디빌딩",
                        hasChatRoom = true,
                    ),
                    ShootingHistoryItem(
                        id = "2",
                        photographerName = "유가영사진",
                        photographerImageUri = "",
                        productName = "인스타 피드꾸미기",
                        price = 12000,
                        status = ShootingStatus.COMPLETED,
                        paymentDate = "2025.03.01",
                        shootingDate = "25.03.24 | 오후2:30",
                        shootingLocation = "종로구 효자로 33 어디어디 어디",
                        hasChatRoom = true,
                    ),
                    ShootingHistoryItem(
                        id = "3",
                        photographerName = "유가영사진",
                        photographerImageUri = "",
                        productName = "인스타 피드꾸미기",
                        price = 12000,
                        status = ShootingStatus.COMPLETED,
                        paymentDate = "2025.03.01",
                        shootingDate = "25.03.24 | 오후2:30",
                        shootingLocation = "종로구 효자로 33",
                        hasChatRoom = false,
                    ),
                )
        }
    }
