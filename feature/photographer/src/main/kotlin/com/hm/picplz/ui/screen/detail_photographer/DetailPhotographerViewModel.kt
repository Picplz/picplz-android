package com.hm.picplz.ui.screen.detail_photographer

import androidx.lifecycle.SavedStateHandle
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
open class DetailPhotographerViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        val photographerId: Int = savedStateHandle.get<Int>("photographerId") ?: 0

        private val _state = MutableStateFlow(DetailPhotographerState.idle())
        val state: StateFlow<DetailPhotographerState> = _state

        private val _sideEffect = MutableSharedFlow<DetailPhotographerSideEffect>()
        val sideEffect: SharedFlow<DetailPhotographerSideEffect> get() = _sideEffect

        fun handleIntent(intent: DetailPhotographerIntent) {
            when (intent) {
                is DetailPhotographerIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.emit(DetailPhotographerSideEffect.NavigateToPrev)
                    }
                }
            }
        }
    }
