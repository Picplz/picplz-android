package com.hm.picplz.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerIntent
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerReviewState
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DetailPhotographerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(DetailPhotographerReviewState.idle())
    val state: StateFlow<DetailPhotographerReviewState> = _state

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