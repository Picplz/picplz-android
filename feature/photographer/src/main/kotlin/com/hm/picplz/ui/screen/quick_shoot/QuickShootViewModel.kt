package com.hm.picplz.ui.screen.quick_shoot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.ui.screen.quick_shoot.handler.LocationHandler
import com.hm.picplz.ui.screen.quick_shoot.handler.PhotographerSearchHandler
import com.hm.picplz.ui.screen.quick_shoot.util.OffsetGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickShootViewModel
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
        private val displayMetricsUtil: com.hm.picplz.common.util.DisplayMetricsUtil,
        private val locationService: LocationService,
    ) : ViewModel() {
        private val _state = MutableStateFlow(QuickShootState.idle())
        val state: StateFlow<QuickShootState> get() = _state

        private val _sideEffect = Channel<QuickShootSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private val locationHandler = LocationHandler()
        private val offsetGenerator = OffsetGenerator(displayMetricsUtil)
        private val photographerSearchHandler = PhotographerSearchHandler(offsetGenerator)

        fun handleIntent(intent: QuickShootIntent) {
            when (intent) {
                is QuickShootIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(QuickShootSideEffect.NavigateToPrev)
                    }
                }

                is QuickShootIntent.GetAddress -> {
                    viewModelScope.launch {
                        locationService.getCurrentLocation(
                            onLocationReceived = { location ->
                                handleIntent(QuickShootIntent.SetCenterCoords(location))
                            },
                            onPermissionDenied = {
                                Log.w("Location", "위치 권한 거부")
                            },
                        )
                    }
                }

                is QuickShootIntent.RequestLocationPermission -> {
                    viewModelScope.launch {
                        _sideEffect.send(QuickShootSideEffect.RequestLocationPermission)
                    }
                }

                is QuickShootIntent.SetLocationPermissionGranted -> {
                    _state.update { it.copy(locationPermissionGranted = intent.granted) }
                }

                is QuickShootIntent.GetCurrentLocation -> {
                    _state.update { it.copy(isFetchingGPS = true) }
                    locationService.getCurrentLocation(
                        onLocationReceived = { location ->
                            _state.update { it.copy(isFetchingGPS = false) }
                            handleIntent(QuickShootIntent.SetCurrentLocation(location))
                        },
                        onPermissionDenied = {
                            _state.update { it.copy(isFetchingGPS = false) }
                            handleIntent(QuickShootIntent.RequestLocationPermission())
                        },
                    )
                }

                is QuickShootIntent.FetchNearbyPhotographers,
                is QuickShootIntent.RefetchNearbyPhotographers,
                -> {
                    viewModelScope.launch {
                        val userLocation = _state.value.userLocation
                        if (userLocation == null) {
                            Log.w("QuickShoot", "User location not available yet")
                            return@launch
                        }
                        handleIntent(QuickShootIntent.SetSelectedPhotographerId(null))
                        handleIntent(QuickShootIntent.SetIsSearchingPhotographer(true))
                        photographerRepository.getNearbyPhotographers(
                            longitude = userLocation.longitude,
                            latitude = userLocation.latitude,
                            distance = 2000,
                        )
                            .onSuccess { nearbyPhotographers ->
                                handleIntent(QuickShootIntent.SetIsSearchingPhotographer(false))
                                handleIntent(QuickShootIntent.SetNearbyPhotographers(nearbyPhotographers))
                                handleIntent(QuickShootIntent.DistributeRandomOffsets(nearbyPhotographers))
                            }
                            .onFailure { error ->
                                handleIntent(QuickShootIntent.SetIsSearchingPhotographer(false))
                                Log.e("FetchPhotographers", "작가 목록 로딩 실패", error)
                            }
                    }
                }

                else -> {
                    val newState =
                        locationHandler.process(intent, _state.value)
                            ?: photographerSearchHandler.process(intent, _state.value)

                    newState?.let { _state.value = it }
                }
            }
        }

        override fun onCleared() {
            super.onCleared()
            locationService.cleanup()
        }
    }
