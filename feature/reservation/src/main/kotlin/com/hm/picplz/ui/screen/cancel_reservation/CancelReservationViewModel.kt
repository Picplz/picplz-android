package com.hm.picplz.ui.screen.cancel_reservation

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
class CancelReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CancelReservationState())
    val state: StateFlow<CancelReservationState> get() = _state

    private val _sideEffect = MutableSharedFlow<CancelReservationSideEffect>()
    val sideEffect: SharedFlow<CancelReservationSideEffect> get() = _sideEffect

    fun handleIntent(intent: CancelReservationIntent) {
        when (intent) {
            is CancelReservationIntent.NavigateToHistory -> {
                emitSideEffect(CancelReservationSideEffect.NavigateToHistory)
            }

            is CancelReservationIntent.NavigateToHome -> {
                emitSideEffect(CancelReservationSideEffect.NavigateToHome)
            }

            is CancelReservationIntent.NavigateBack -> {
                emitSideEffect(CancelReservationSideEffect.NavigateToPrev)
            }
        }
    }

    private fun emitSideEffect(sideEffect: CancelReservationSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }
}
