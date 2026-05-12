package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetNearbyAreasUseCase
import com.hm.picplz.domain.usecase.GetPhotographerActiveAreasUseCase
import com.hm.picplz.domain.usecase.SearchAreasUseCase
import com.hm.picplz.domain.usecase.UpdatePhotographerActiveAreasUseCase
import com.hm.picplz.feature.mypage.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_ACTIVE_AREA_COUNT = 5
private const val NEARBY_AREA_RADIUS = 3

@HiltViewModel
class MyPagePhotographerActiveAreaEditViewModel
    @Inject
    constructor(
        private val getPhotographerActiveAreasUseCase: GetPhotographerActiveAreasUseCase,
        private val updatePhotographerActiveAreasUseCase: UpdatePhotographerActiveAreasUseCase,
        private val searchAreasUseCase: SearchAreasUseCase,
        private val getNearbyAreasUseCase: GetNearbyAreasUseCase,
        private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MyPagePhotographerActiveAreaEditState.idle())
        val state: StateFlow<MyPagePhotographerActiveAreaEditState> get() = _state

        private val _sideEffect = Channel<MyPagePhotographerActiveAreaEditSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private var searchJob: Job? = null

        fun handleIntent(intent: MyPagePhotographerActiveAreaEditIntent) {
            when (intent) {
                is MyPagePhotographerActiveAreaEditIntent.LoadActiveAreas -> loadActiveAreas(intent.photographerId)
                MyPagePhotographerActiveAreaEditIntent.NavigateBack -> sendNavigateBack()
                is MyPagePhotographerActiveAreaEditIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
                MyPagePhotographerActiveAreaEditIntent.SearchArea -> searchArea()
                is MyPagePhotographerActiveAreaEditIntent.ToggleAreaSelection -> toggleAreaSelection(intent.area)
                is MyPagePhotographerActiveAreaEditIntent.RemoveSelectedArea -> removeSelectedArea(intent.area)
                MyPagePhotographerActiveAreaEditIntent.Save -> saveActiveAreas()
                MyPagePhotographerActiveAreaEditIntent.DismissToast -> dismissToast()
            }
        }

        private fun loadActiveAreas(photographerId: Int) {
            if (photographerId <= 0) {
                _state.update { it.copy(toastMessageResId = R.string.active_area_edit_load_failed) }
                return
            }
            if (_state.value.photographerId == photographerId && _state.value.originalAreas.isNotEmpty()) return

            _state.update { it.copy(photographerId = photographerId, isLoading = true) }
            viewModelScope.launch {
                getPhotographerActiveAreasUseCase(photographerId.toLong())
                    .onSuccess { areas ->
                        _state.update {
                            it.copy(
                                originalAreas = areas,
                                selectedAreas = areas,
                                isLoading = false,
                            )
                        }
                    }.onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                toastMessageResId = R.string.active_area_edit_load_failed,
                            )
                        }
                    }
            }
        }

        private fun updateSearchQuery(query: String) {
            _state.update {
                it.copy(
                    searchQuery = query,
                    hasSearchCompleted = false,
                    searchResults = if (query.isBlank()) emptyList() else it.searchResults,
                )
            }
        }

        private fun searchArea() {
            val keyword = _state.value.searchQuery.trim()
            searchJob?.cancel()
            if (keyword.isBlank()) {
                loadNearbyAreas()
                return
            }

            _state.update { it.copy(isSearching = true) }
            searchJob =
                viewModelScope.launch {
                    searchAreasUseCase(keyword)
                        .onSuccess { areas ->
                            if (_state.value.searchQuery.trim() != keyword) return@onSuccess
                            _state.update {
                                it.copy(
                                    searchResults = areas,
                                    isSearching = false,
                                    hasSearchCompleted = true,
                                )
                            }
                        }.onFailure {
                            if (_state.value.searchQuery.trim() != keyword) return@onFailure
                            _state.update {
                                it.copy(
                                    searchResults = emptyList(),
                                    isSearching = false,
                                    hasSearchCompleted = true,
                                    toastMessageResId = R.string.active_area_edit_search_failed,
                                )
                            }
                        }
                }
        }

        private fun loadNearbyAreas() {
            _state.update { it.copy(isSearching = true, hasSearchCompleted = false) }
            searchJob = null
            getCurrentLocationUseCase(
                onLocationReceived = { location ->
                    viewModelScope.launch {
                        getNearbyAreasUseCase(
                            rad = NEARBY_AREA_RADIUS,
                            lat = location.latitude,
                            lng = location.longitude,
                        ).onSuccess { areas ->
                            if (_state.value.searchQuery.isNotBlank()) return@onSuccess
                            _state.update {
                                it.copy(
                                    searchResults = areas,
                                    isSearching = false,
                                    hasSearchCompleted = false,
                                )
                            }
                        }.onFailure {
                            if (_state.value.searchQuery.isNotBlank()) return@onFailure
                            _state.update {
                                it.copy(
                                    searchResults = emptyList(),
                                    isSearching = false,
                                    hasSearchCompleted = false,
                                )
                            }
                        }
                    }
                },
                onPermissionDenied = {
                    _state.update {
                        it.copy(
                            searchResults = emptyList(),
                            isSearching = false,
                            hasSearchCompleted = false,
                        )
                    }
                },
            )
        }

        private fun toggleAreaSelection(area: Area) {
            _state.update { currentState ->
                val isSelected = currentState.selectedAreas.any { it.id == area.id }
                when {
                    isSelected -> {
                        currentState.copy(toastMessageResId = R.string.active_area_edit_already_selected)
                    }
                    currentState.selectedAreas.size >= MAX_ACTIVE_AREA_COUNT -> {
                        currentState.copy(toastMessageResId = R.string.active_area_edit_max_count_error)
                    }
                    else -> currentState.copy(selectedAreas = currentState.selectedAreas + area)
                }
            }
        }

        private fun removeSelectedArea(area: Area) {
            _state.update { currentState ->
                currentState.copy(
                    selectedAreas = currentState.selectedAreas.filterNot { it.id == area.id },
                )
            }
        }

        private fun saveActiveAreas() {
            val selectedAreas = _state.value.selectedAreas
            if (selectedAreas.isEmpty()) {
                _state.update { it.copy(toastMessageResId = R.string.active_area_edit_empty_error) }
                return
            }
            if (!_state.value.isSaveEnabled) return

            _state.update { it.copy(isSaving = true) }
            viewModelScope.launch {
                updatePhotographerActiveAreasUseCase(selectedAreas)
                    .onSuccess {
                        _state.update { it.copy(isSaving = false) }
                        _sideEffect.send(MyPagePhotographerActiveAreaEditSideEffect.NavigateBack)
                    }.onFailure {
                        _state.update {
                            it.copy(
                                isSaving = false,
                                toastMessageResId = R.string.active_area_edit_save_failed,
                            )
                        }
                    }
            }
        }

        private fun sendNavigateBack() {
            viewModelScope.launch {
                _sideEffect.send(MyPagePhotographerActiveAreaEditSideEffect.NavigateBack)
            }
        }

        private fun dismissToast() {
            _state.update { it.copy(toastMessageResId = null) }
        }
    }
