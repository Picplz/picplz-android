package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotographerMainViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(PhotographerMainState.idle())
    val state: StateFlow<PhotographerMainState> get() = _state
}