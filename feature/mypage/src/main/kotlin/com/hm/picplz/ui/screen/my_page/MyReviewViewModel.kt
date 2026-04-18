package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReviewViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(MyReviewState.idle())
        val state: StateFlow<MyReviewState> = _state

        private val _sideEffect = Channel<MyReviewSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: MyReviewIntent) {
            when (intent) {
                is MyReviewIntent.NavigateBack -> {
                    sendSideEffect(MyReviewSideEffect.NavigateBack)
                }

                is MyReviewIntent.ToggleReviewExpansion -> {
                    _state.update { currentState ->
                        val expandedReviewIds = currentState.expandedReviewIds.toMutableSet()
                        if (!expandedReviewIds.add(intent.reviewId)) {
                            expandedReviewIds.remove(intent.reviewId)
                        }
                        currentState.copy(expandedReviewIds = expandedReviewIds)
                    }
                }

                is MyReviewIntent.RequestDelete -> {
                    _state.update { it.copy(pendingDeleteReviewId = intent.reviewId) }
                }

                is MyReviewIntent.DismissDeleteDialog -> {
                    _state.update { it.copy(pendingDeleteReviewId = null) }
                }

                is MyReviewIntent.ConfirmDelete -> {
                    _state.update { currentState ->
                        val reviewId = currentState.pendingDeleteReviewId ?: return@update currentState
                        currentState.copy(
                            reviews = currentState.reviews.filterNot { review -> review.id == reviewId },
                            expandedReviewIds = currentState.expandedReviewIds - reviewId,
                            pendingDeleteReviewId = null,
                        )
                    }
                }
            }
        }

        private fun sendSideEffect(effect: MyReviewSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }
    }
