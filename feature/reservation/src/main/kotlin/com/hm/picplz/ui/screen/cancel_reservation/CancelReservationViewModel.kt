package com.hm.picplz.ui.screen.cancel_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.CancelReservation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelReservationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val orderId: String = savedStateHandle.toRoute<CancelReservation>().orderId

    private val _state = MutableStateFlow(CancelReservationState.idle(orderId))
    val state: StateFlow<CancelReservationState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CancelReservationSideEffect>()
    val sideEffect: SharedFlow<CancelReservationSideEffect> = _sideEffect.asSharedFlow()

    fun handleIntent(intent: CancelReservationIntent) {
        when (intent) {
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

            is CancelReservationIntent.UpdateAgreement -> {
                _state.update { it.copy(isAgreementChecked = intent.isChecked) }
            }

            is CancelReservationIntent.OnBackClick -> {
                handleBackClick()
            }

            is CancelReservationIntent.OnNextClick -> {
                handleNextClick()
            }
        }
    }

    private fun handleBackClick() {
        val currentPage = _state.value.currentPagerPage
        when (currentPage) {
            CancelReservationPagerPage.REASON_INPUT -> {
                emitSideEffect(CancelReservationSideEffect.NavigateBack)
            }
            CancelReservationPagerPage.REFUND_GUIDE -> {
                _state.update { it.copy(currentPagerPage = CancelReservationPagerPage.REASON_INPUT) }
            }
        }
    }

    private fun handleNextClick() {
        val currentPage = _state.value.currentPagerPage
        when (currentPage) {
            CancelReservationPagerPage.REASON_INPUT -> {
                _state.update { it.copy(currentPagerPage = CancelReservationPagerPage.REFUND_GUIDE) }
            }
            CancelReservationPagerPage.REFUND_GUIDE -> {
                emitSideEffect(CancelReservationSideEffect.ShowCancelConfirmModal)
            }
        }
    }

    private fun emitSideEffect(sideEffect: CancelReservationSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }
}
