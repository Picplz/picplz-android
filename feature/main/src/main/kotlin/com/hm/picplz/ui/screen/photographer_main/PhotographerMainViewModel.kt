package com.hm.picplz.ui.screen.photographer_main

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
class PhotographerMainViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(PhotographerMainState.idle())
        val state: StateFlow<PhotographerMainState> get() = _state

        private val _sideEffect = Channel<PhotographerMainSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: PhotographerMainIntent) {
            when (intent) {
                is PhotographerMainIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(PhotographerMainSideEffect.NavigateToPrev)
                    }
                }
                is PhotographerMainIntent.Navigate -> {
                    viewModelScope.launch {
                        _sideEffect.send(PhotographerMainSideEffect.Navigate(intent.destination))
                    }
                }
                is PhotographerMainIntent.SetIsActive -> {
                    _state.update { it.copy(isActive = intent.isActive) }
                }
                is PhotographerMainIntent.SetIsModalOpen -> {
                    _state.update { it.copy(isModalOpen = intent.isModalOpen) }
                }
                is PhotographerMainIntent.ToggleEquipmentEnabled -> {
                    _state.update {
                        it.copy(
                            equipmentList =
                                it.equipmentList.map { equipment ->
                                    if (equipment.id == intent.deviceId) {
                                        equipment.copy(isEnabled = !equipment.isEnabled)
                                    } else {
                                        equipment
                                    }
                                },
                        )
                    }
                }
            }
        }
    }
