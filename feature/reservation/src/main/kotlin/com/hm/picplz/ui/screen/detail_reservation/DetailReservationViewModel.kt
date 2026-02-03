package com.hm.picplz.ui.screen.detail_reservation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DetailReservationState())
    val state: StateFlow<DetailReservationState> get() = _state
}
