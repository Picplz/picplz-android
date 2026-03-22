package com.hm.picplz.ui.screen.cancel_reservation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CancelReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CancelReservationState())
    val state: StateFlow<CancelReservationState> get() = _state

    fun handleIntent(intent: CancelReservationIntent) { }
}
