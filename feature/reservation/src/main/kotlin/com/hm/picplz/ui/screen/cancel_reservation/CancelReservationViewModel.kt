package com.hm.picplz.ui.screen.cancel_reservation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CancelReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CancelReservationState.idle())
    val state: StateFlow<CancelReservationState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CancelReservationSideEffect>()
    val sideEffect: SharedFlow<CancelReservationSideEffect> = _sideEffect.asSharedFlow()

    fun handleIntent(intent: CancelReservationIntent) {
        when (intent) {
            is CancelReservationIntent.Initialize -> {
                _state.update { it.copy(orderId = intent.orderId) }
            }

            is CancelReservationIntent.ToggleReason -> {
                _state.update { currentState ->
                    val newReasons = currentState.selectedReasons.toMutableSet()
                    if (newReasons.contains(intent.reason)) {
                        newReasons.remove(intent.reason)
                    } else {
                        newReasons.add(intent.reason)
                    }
                    currentState.copy(selectedReasons = newReasons)
                }
            }

            is CancelReservationIntent.UpdateDirectInput -> {
                _state.update { it.copy(directInputText = intent.text.take(100)) }
            }

            is CancelReservationIntent.UpdatePagerPage -> {
                _state.update { it.copy(currentPagerPage = intent.page) }
            }

            is CancelReservationIntent.OnBackClick -> {
                emitSideEffect(CancelReservationSideEffect.NavigateBack)
            }

            is CancelReservationIntent.OnNextClick -> {
                // 페이지에 따라 다른 동작
                val currentPage = _state.value.currentPagerPage
                when (currentPage) {
                    CancelReservationPagerPage.REASON_INPUT -> {
                        // 취소 사유 입력 완료 → 환불 안내로
                        _state.update { it.copy(currentPagerPage = CancelReservationPagerPage.REFUND_GUIDE) }
                    }
                    CancelReservationPagerPage.REFUND_GUIDE -> {
                        // 환불 안내 완료 → 취소 확인 모달
                        emitSideEffect(CancelReservationSideEffect.ShowCancelConfirmModal)
                    }
                }
            }
        }
    }

    private fun emitSideEffect(sideEffect: CancelReservationSideEffect) {
        _sideEffect.tryEmit(sideEffect)
    }
}
