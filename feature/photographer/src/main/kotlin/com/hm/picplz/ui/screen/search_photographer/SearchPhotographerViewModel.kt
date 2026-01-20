package com.hm.picplz.ui.screen.search_photographer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerSideEffect
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState
import com.hm.picplz.ui.screen.search_photographer.handler.LocationHandler
import com.hm.picplz.ui.screen.search_photographer.handler.PhotographerSearchHandler
import com.hm.picplz.ui.screen.search_photographer.util.OffsetGenerator
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotographerViewModel @Inject constructor(
    private val photographerRepository: PhotographerRepository,
    private val displayMetricsUtil: com.hm.picplz.common.util.DisplayMetricsUtil,
    private val locationService: LocationService
) : ViewModel() {
    private val _state = MutableStateFlow(SearchPhotographerState.idle())
    val state : StateFlow<SearchPhotographerState> get() = _state

    private val _sideEffect = MutableSharedFlow<SearchPhotographerSideEffect>()
    val sideEffect: SharedFlow<SearchPhotographerSideEffect> get() = _sideEffect

    private val locationHandler = LocationHandler()
    private val offsetGenerator = OffsetGenerator(displayMetricsUtil)
    private val photographerSearchHandler = PhotographerSearchHandler(displayMetricsUtil, offsetGenerator)

    init {
        handleIntent(SearchPhotographerIntent.FetchNearbyPhotographers)
    }

    fun handleIntent(intent: SearchPhotographerIntent) {
        when (intent) {
            is SearchPhotographerIntent.NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SearchPhotographerSideEffect.NavigateToPrev)
                }
            }

            is SearchPhotographerIntent.GetAddress -> {
                viewModelScope.launch {
                    locationService.getCurrentLocation(
                        onLocationReceived = { location ->
                            handleIntent(SearchPhotographerIntent.SetCenterCoords(location))
                        },
                        onPermissionDenied = {
                            Log.w("Location", "위치 권한 거부")
                        }
                    )
                }
            }

            is SearchPhotographerIntent.RequestLocationPermission -> {
                viewModelScope.launch {
                    _sideEffect.emit(SearchPhotographerSideEffect.RequestLocationPermission)
                }
            }

            is SearchPhotographerIntent.GetCurrentLocation -> {
                _state.update { it.copy(isFetchingGPS = true) }
                locationService.getCurrentLocation(
                    onLocationReceived = { location ->
                        _state.update { it.copy(isFetchingGPS = false) }
                        handleIntent(SearchPhotographerIntent.SetCurrentLocation(location))
                    },
                    onPermissionDenied = {
                        _state.update { it.copy(isFetchingGPS = false) }
                        handleIntent(SearchPhotographerIntent.RequestLocationPermission())
                    }
                )
            }

            is SearchPhotographerIntent.FetchNearbyPhotographers,
            is SearchPhotographerIntent.RefetchNearbyPhotographers -> {
                viewModelScope.launch {
                    val dummyUserLocation = LatLng.from(37.402960, 127.115587)
                    val dummyUserAddress = "종로구 무악동"
                    handleIntent(SearchPhotographerIntent.SetSelectedPhotographerId(null))
                    handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(true))
                    photographerRepository.getNearbyPhotographers(
                        userLocation = dummyUserLocation,
                        distanceLimit = 2,
                        countLimit = 5,
                        userAddress = dummyUserAddress,
                    )
                        .onSuccess { nearbyPhotographers ->
                            handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(false))
                            handleIntent(SearchPhotographerIntent.SetNearbyPhotographers(nearbyPhotographers))
                            handleIntent(SearchPhotographerIntent.DistributeRandomOffsets(nearbyPhotographers))
                        }
                        .onFailure { error ->
                            handleIntent(SearchPhotographerIntent.SetIsSearchingPhotographer(false))
                            Log.e("FetchPhotographers", "작가 목록 로딩 실패", error)
                        }
                }
            }

            else -> {
                val newState = locationHandler.process(intent, _state.value)
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
