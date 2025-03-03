package com.hm.picplz.viewmodel

import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.source.KakaoMapSource
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.location.LocationListener
import androidx.compose.ui.geometry.Offset
import com.hm.picplz.data.repository.PhotographerRepository
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.ui.model.FilteredPhotographers
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerSideEffect
import com.hm.picplz.utils.DisplayMetricsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SearchPhotographerViewModel @Inject constructor(
    private val photographerRepository: PhotographerRepository,
    private val displayMetricsUtil: DisplayMetricsUtil,
    private val locationService: LocationService,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchPhotographerState.idle())
    val state : StateFlow<SearchPhotographerState> get() = _state

    private val _sideEffect = MutableSharedFlow<SearchPhotographerSideEffect>()
    val sideEffect: SharedFlow<SearchPhotographerSideEffect> get() = _sideEffect

    init {
        handleIntent(SearchPhotographerIntent.FetchNearbyPhotographers)
    }

    private val kakaoSource = KakaoMapSource()

    fun handleIntent(intent: SearchPhotographerIntent) {
        when (intent) {
            is SearchPhotographerIntent.NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SearchPhotographerSideEffect.NavigateToPrev)
                }
            }
            is SearchPhotographerIntent.SetAddress -> {
                _state.update { it.copy(address = intent.address) }
            }
            is SearchPhotographerIntent.GetAddress -> {
                viewModelScope.launch {
                    kakaoSource.getAddressFromCoords(KaKaoAddressRequest(intent.Coords.longitude.toString(), intent.Coords.latitude.toString()))
                        .onSuccess { response ->
                            val twoDepthRegion = response.documents.firstOrNull()?.address?.region_2depth_name
                                ?.split(" ")
                                ?.lastOrNull() ?: ""
                            val threeDepthRegion = response.documents.firstOrNull()?.address?.region_3depth_name ?: ""
                            val address = "$twoDepthRegion $threeDepthRegion"
                            handleIntent(SearchPhotographerIntent.SetAddress(address))
                        }
                        .onFailure { error ->
                            Log.e("kakaoMapAddressSearch", "좌표 검색 실패 : ", error)
                        }
                }
            }
            is SearchPhotographerIntent.SetCenterCoords -> {
                _state.update { it.copy(centerCoords = intent.centerCoords) }
                handleIntent(SearchPhotographerIntent.GetAddress(intent.centerCoords))
            }
            is SearchPhotographerIntent.SetCurrentLocation -> {
                _state.update { it.copy(
                    userLocation = intent.location,
                    isFetchingGPS = false
                ) }
                handleIntent(SearchPhotographerIntent.GetAddress(intent.location))
            }
            is SearchPhotographerIntent.GetCurrentLocation -> {
                locationService.getCurrentLocation(intent.context) { location ->
                    handleIntent(SearchPhotographerIntent.SetCurrentLocation(location))
                }
            }
            is SearchPhotographerIntent.SetIsSearchingPhotographer -> {
                _state.update { it.copy(isSearchingPhotographer = intent.isSearchingPhotographer) }
            }
            is SearchPhotographerIntent.SetNearbyPhotographers -> {
                _state.update { it.copy(nearbyPhotographers = intent.nearbyPhotographers)}
            }
            is SearchPhotographerIntent.FetchNearbyPhotographers,
            is SearchPhotographerIntent.RefetchNearbyPhotographers -> {
                viewModelScope.launch {
//                    val userLocation = state.value.userLocation ?: return@launch
                    val dummyUserLocation = LatLng.from(37.402960, 127.115587)
//                    val userAddress = state.value.address ?: return@launch
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
            is SearchPhotographerIntent.DistributeRandomOffsets -> {
                val randomOffsets = generateNonOverlappingOffsets(intent.photographers)
                _state.update { it.copy(randomOffsets = randomOffsets) }
            }
            is SearchPhotographerIntent.SetSelectedPhotographerId -> {
                _state.update { it.copy(
                    selectedPhotographerId = if (state.value.selectedPhotographerId == intent.photographerId) null else intent.photographerId
                )}
            }
            is SearchPhotographerIntent.SetSheetMaxHeight -> {
                _state.update { it.copy(
                    sheetMaxHeight = intent.maxHeight
                )}
            }
            is SearchPhotographerIntent.SetSheetPeekHeight -> {
                _state.update { it.copy(
                    sheetPeekHeight = intent.peekHeight
                )}
            }
            is SearchPhotographerIntent.CenterSelectedPhotographer -> {
                val newOffset = Offset(
                    x = -intent.offset.x,
                    y = -intent.offset.y
                )
                _state.update { it.copy(centerOffset = newOffset) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationService.cleanup()
    }

    class OffsetGenerationFailedException : Exception("전체 위치 생성 최종 실패")

    private fun generateNonOverlappingOffsets(photographers: FilteredPhotographers): Map<Int, Offset> {
        val maxAttempts = 1000

        for (attempt in 1..maxAttempts) {
            try {
                return tryGenerateOffsets(photographers)
            } catch (e: OffsetGenerationException) {
                continue
            }
        }

        throw OffsetGenerationFailedException()
    }

    private class OffsetGenerationException : Exception("개별 위치 생성 실패")


    private fun tryGenerateOffsets(photographers: FilteredPhotographers): Map<Int, Offset> {
        val offsets = mutableMapOf<Int, Offset>()
        val minDistance = 110f

        val maxSingleAttempts = 100

        val (maxOffsetX, innerCircleMaxOffsetX, outerCircleMinOffsetX) = displayMetricsUtil.calculateOffsetLimits()

        val center = Offset(0f, 0f)

        fun generateOffset(): Offset {
            return Offset(
                (Random.nextFloat() * 2 - 1) * maxOffsetX,
                (Random.nextFloat() * 2 - 1) * maxOffsetX
            )
        }

        if (photographers.inactive.isEmpty()) {
            photographers.active.forEachIndexed{ index, photographer ->
                var newOffset: Offset
                var attempts = 0

                do {
                    attempts++
                    newOffset = generateOffset()

                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (
                    offsets.values.any { existingOffset ->
                        displayMetricsUtil.calculateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < minDistance) ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) > innerCircleMaxOffsetX) ||
                    (index >= 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < outerCircleMinOffsetX)
                )
                offsets[photographer.id] = newOffset
            }
        } else {
            photographers.active.forEachIndexed { index, photographer ->
                var attempts = 0
                var newOffset: Offset

                do {
                    attempts++
                    newOffset = generateOffset()
                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (
                    offsets.values.any { existingOffset ->
                        displayMetricsUtil.calculateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < minDistance) ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) > innerCircleMaxOffsetX) ||
                    (index >= 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < outerCircleMinOffsetX)
                )
                offsets[photographer.id] = newOffset
            }
            photographers.inactive.forEach { photographer ->
                var newOffset: Offset
                var attempts = 0
                do {
                    attempts++
                    newOffset = generateOffset()
                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (
                    offsets.values.any { existingOffset ->
                        displayMetricsUtil.calculateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    displayMetricsUtil.calculateScreenDistance(center, newOffset) < outerCircleMinOffsetX
                )
                offsets[photographer.id] = newOffset
            }
        }
        return offsets
    }
}