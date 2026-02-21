package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.ActiveAreaRequest
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.PhotographerCameraRequest
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.AddressService
import com.hm.picplz.data.service.CameraService
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.AreaSearchHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.CareerHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.DeviceHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.VibeChipHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpPhotographerViewModel
    @Inject
    constructor(
        private val addressService: AddressService,
        private val locationService: LocationService,
        private val cameraService: CameraService,
        private val photographerService: PhotographerService,
        private val tokenManager: TokenManager,
    ) : ViewModel() {
        private val _state = MutableStateFlow<SignUpPhotographerState>(SignUpPhotographerState.idle())
        val state: StateFlow<SignUpPhotographerState> get() = _state

        private val _sideEffect = Channel<SignUpPhotographerSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private val vibeChipHandler = VibeChipHandler()
        private val areaSearchHandler = AreaSearchHandler()
        private val deviceHandler = DeviceHandler()
        private val careerHandler = CareerHandler()

        init {
            loadNearbyAreasOnInit()
            loadCameras()
        }

        private fun loadCameras() {
            _state.update { it.copy(isCamerasLoading = true) }
            viewModelScope.launch {
                cameraService.getCameraList()
                    .onSuccess { cameraListData ->
                        _state.update {
                            it.copy(
                                availableCameraBrands = cameraListData.brands,
                                availableCameraTypes = cameraListData.types,
                                isCamerasLoading = false,
                            )
                        }
                        Log.d("CameraLoad", "카메라 목록 로딩 성공: ${cameraListData.brands.size}개 브랜드")
                    }
                    .onFailure { error ->
                        _state.update { it.copy(isCamerasLoading = false) }
                        Log.e("CameraLoad", "카메라 목록 로딩 실패", error)
                    }
            }
        }

        private fun loadNearbyAreasOnInit() {
            viewModelScope.launch {
                locationService.getCurrentLocation(
                    onLocationReceived = { location ->
                        _state.update { it.copy(hasLocationPermission = true) }

                        loadNearbyAreas(
                            lat = location.latitude,
                            lng = location.longitude,
                        )
                    },
                    onPermissionDenied = {
                        Log.w("LocationInfo", "위치 권한 거부됨")

                        _state.update {
                            it.copy(
                                hasLocationPermission = false,
                                searchResults = emptyList(),
                                isSearching = false,
                                searchError = "위치 권한이 필요합니다",
                            )
                        }
                    },
                )
            }
        }

        private fun loadNearbyAreas(
            lat: Double,
            lng: Double,
        ) {
            _state.update { it.copy(isSearching = true) }

            viewModelScope.launch {
                addressService.getNearbyAreas(3, lat, lng)
                    .onSuccess { nearbyAreas ->
                        _state.update {
                            it.copy(
                                searchResults = nearbyAreas,
                                isSearching = false,
                                searchError = null,
                            )
                        }
                        Log.d("AddressSearch", "근처 지역 로딩 성공: ${nearbyAreas.size}개")
                    }
                    .onFailure { error ->
                        _state.update {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                searchError = "근처 지역을 불러올 수 없습니다",
                            )
                        }
                        Log.e("AddressSearch", "근처 지역 로딩 실패", error)
                    }
            }
        }

        fun handleIntent(intent: SignUpPhotographerIntent) {
            when (intent) {
                is SignUpPhotographerIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpPhotographerSideEffect.NavigateToPrev)
                    }
                }

                is SignUpPhotographerIntent.SetUserInfo -> {
                    _state.update { it.copy(userInfo = intent.userInfo) }
                }

                is SignUpPhotographerIntent.SetIsOpenDialog -> {
                    _state.update { it.copy(showInfoDialog = intent.isOpen) }
                }

                is SignUpPhotographerIntent.DismissToast -> {
                    _state.update { it.copy(showToast = false, toastMessage = null) }
                }

                is SignUpPhotographerIntent.Navigate -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpPhotographerSideEffect.Navigate(intent.destination))
                    }
                }

                is SignUpPhotographerIntent.NavigateWithSubmit -> {
                    viewModelScope.launch {
                        if (_state.value.isSubmitting) {
                            return@launch
                        }

                        _state.update { it.copy(isSubmitting = true, error = null) }

                        val socialCode = tokenManager.getSocialCode()
                        if (socialCode == null) {
                            _state.update {
                                it.copy(
                                    isSubmitting = false,
                                    error = IllegalStateException("소셜 로그인 정보가 없습니다"),
                                )
                            }
                            return@launch
                        }

                        val currentState = _state.value
                        val request =
                            CreatePhotographerRequest(
                                nickname = currentState.userInfo.nickname.orEmpty(),
                                socialEmail = tokenManager.getSocialEmail(),
                                socialProvider = tokenManager.getSocialProvider(),
                                socialCode = socialCode,
                                profileImage = currentState.userInfo.profileImageObjectKey,
                                photoMoods = currentState.selectedVibeChipList.map { it.label },
                                activeAreas =
                                    currentState.selectedAreas.mapIndexed { index, area ->
                                        ActiveAreaRequest(
                                            code = area.id,
                                            priority = index + 1,
                                        )
                                    },
                                cameras =
                                    currentState.cameraDevices.map {
                                        PhotographerCameraRequest(
                                            type = "CAMERA",
                                            brand = it.companyName,
                                            name = it.modelName,
                                            cameraType = it.cameraType,
                                        )
                                    },
                            )

                        photographerService.createPhotographer(request)
                            .onSuccess {
                                val user = currentState.userInfo
                                _state.update { it.copy(isSubmitting = false) }
                                _sideEffect.send(
                                    SignUpPhotographerSideEffect.NavigateToSignUpCompletion(user),
                                )
                            }
                            .onFailure { error ->
                                _state.update {
                                    it.copy(
                                        isSubmitting = false,
                                        error = error,
                                    )
                                }
                            }
                    }
                }

                is SignUpPhotographerIntent.SearchArea -> {
                    handleSearchAreaIntent(intent)
                }

                is SignUpPhotographerIntent.AddCurrentDeviceToList -> {
                    handleAddCurrentDeviceToList(intent)
                }

                else -> {
                    val newState =
                        vibeChipHandler.handleIntent(intent, _state.value)
                            ?: areaSearchHandler.handleIntent(intent, _state.value)
                            ?: deviceHandler.handleIntent(intent, _state.value)
                            ?: careerHandler.handleIntent(intent, _state.value)

                    _state.update { newState ?: it }
                }
            }
        }

        private fun handleAddCurrentDeviceToList(intent: SignUpPhotographerIntent.AddCurrentDeviceToList) {
            val newState = deviceHandler.handleIntent(intent, _state.value)
            newState?.let { state ->
                _state.update { state }
                if (!state.showToast) {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpPhotographerSideEffect.NavigateToPrev)
                    }
                }
            }
        }

        private fun handleSearchAreaIntent(intent: SignUpPhotographerIntent.SearchArea) {
            viewModelScope.launch {
                if (intent.keyword.isBlank()) {
                    _state.update {
                        it.copy(
                            hasSearchCompleted = false,
                            searchResults = emptyList(),
                        )
                    }
                    loadNearbyAreasOnInit()
                    return@launch
                }

                _state.update {
                    it.copy(
                        isSearching = true,
                        hasSearchCompleted = false,
                    )
                }

                addressService.searchArea(intent.keyword)
                    .onSuccess { searchedAreas ->
                        _state.update {
                            it.copy(
                                searchResults = searchedAreas,
                                isSearching = false,
                                hasSearchCompleted = true,
                                searchError = null,
                            )
                        }
                    }
                    .onFailure { error ->
                        _state.update {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                hasSearchCompleted = true,
                                searchError = "검색 중 오류가 발생했습니다",
                            )
                        }
                        Log.e("AddressSearch", "지역 검색 실패", error)
                    }
            }
        }
    }
