package com.hm.picplz.ui.screen.detail_photographer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.service.PhotographerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DetailPhotographerViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val photographerService: PhotographerService,
    ) : ViewModel() {
        val photographerId: Int = savedStateHandle.get<Int>("photographerId") ?: 0

        private val _state =
            MutableStateFlow(
                if (photographerId == DEV_BLOCKED_PHOTOGRAPHER_ID) {
                    DetailPhotographerState.blocked()
                } else {
                    DetailPhotographerState.idle()
                },
            )
        val state: StateFlow<DetailPhotographerState> = _state

        private val _sideEffect = Channel<DetailPhotographerSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            loadPhotographerInfo()
        }

        fun handleIntent(intent: DetailPhotographerIntent) {
            when (intent) {
                is DetailPhotographerIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(DetailPhotographerSideEffect.NavigateToPrev)
                    }
                }
                is DetailPhotographerIntent.ToggleFollow -> {
                    _state.update { it.copy(isFollow = !it.isFollow) }
                }
                is DetailPhotographerIntent.ToggleInfoExpanded -> {
                    _state.update { it.copy(isInfoExpanded = !it.isInfoExpanded) }
                }
                is DetailPhotographerIntent.ToggleBlock -> {
                    val wasBlocked = _state.value.isBlocked
                    val name = _state.value.profileInfo.name
                    _state.update {
                        it.copy(
                            isBlocked = !it.isBlocked,
                            toastMessage = if (!wasBlocked) "'$name'님이 차단되었습니다." else null,
                        )
                    }
                }
                is DetailPhotographerIntent.ToggleAreaExpanded -> {
                    _state.update { it.copy(isAreaExpanded = !it.isAreaExpanded) }
                }
                is DetailPhotographerIntent.ToggleMenuSheet -> {
                    _state.update { it.copy(isMenuSheetVisible = !it.isMenuSheetVisible) }
                }
                is DetailPhotographerIntent.SelectReviewSort -> {
                    _state.update {
                        it.copy(
                            reviewSortType = intent.sortType,
                            isSortSheetVisible = false,
                        )
                    }
                }
                is DetailPhotographerIntent.ToggleSortSheet -> {
                    _state.update { it.copy(isSortSheetVisible = !it.isSortSheetVisible) }
                }
                is DetailPhotographerIntent.ShowFullScreenPhoto -> {
                    _state.update { it.copy(fullScreenImageUri = intent.imageUri) }
                }
                is DetailPhotographerIntent.DismissFullScreenPhoto -> {
                    _state.update { it.copy(fullScreenImageUri = null) }
                }
                is DetailPhotographerIntent.ToggleReportSheet -> {
                    _state.update {
                        it.copy(isReportSheetVisible = !it.isReportSheetVisible)
                    }
                }
                is DetailPhotographerIntent.SwitchReview -> {
                    _state.update { it.copy(currentReviewIndex = intent.reviewIndex) }
                }
                is DetailPhotographerIntent.DismissToast -> {
                    _state.update { it.copy(toastMessage = null) }
                }
            }
        }

        companion object {
            const val DEV_BLOCKED_PHOTOGRAPHER_ID = -1
        }

        private fun loadPhotographerInfo() {
            viewModelScope.launch {
                photographerService.getPhotographerInfo(photographerId.toLong())
                    .onSuccess { info ->
                        _state.update { it.copy(profileInfo = info) }
                    }
            }
        }
    }
