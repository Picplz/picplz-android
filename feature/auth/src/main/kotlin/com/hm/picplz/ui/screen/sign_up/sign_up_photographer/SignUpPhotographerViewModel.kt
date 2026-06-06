package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.error.AppError
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.AddressService
import com.hm.picplz.data.service.CameraService
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.PhotographerSignup
import com.hm.picplz.domain.model.PhotographerSignupActiveArea
import com.hm.picplz.domain.model.PhotographerSignupCamera
import com.hm.picplz.domain.model.PhotographerSignupCameraType
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.AreaSearchHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.CareerHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.DeviceHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler.VibeChipHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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

        private val autoSearchRequests = MutableSharedFlow<String>(extraBufferCapacity = 1)
        private val manualSearchRequests = MutableSharedFlow<String>(extraBufferCapacity = 1)

        private val vibeChipHandler = VibeChipHandler()
        private val areaSearchHandler = AreaSearchHandler()
        private val deviceHandler = DeviceHandler()
        private val careerHandler = CareerHandler()

        init {
            observeSearchRequests()
            loadNearbyAreasOnInit()
            loadCameras()
        }

        @OptIn(FlowPreview::class)
        private fun observeSearchRequests() {
            viewModelScope.launch {
                autoSearchRequests
                    .debounce(300L)
                    .map { it.trim() }
                    .distinctUntilChanged()
                    .collectLatest { keyword ->
                        executeSearchArea(keyword)
                    }
            }

            viewModelScope.launch {
                manualSearchRequests
                    .map { it.trim() }
                    .collectLatest { keyword ->
                        executeSearchArea(keyword)
                    }
            }
        }

        private fun loadCameras() {
            _state.update { it.copy(isCamerasLoading = true, cameraLoadError = null) }
            viewModelScope.launch {
                cameraService.getCameraList()
                    .onSuccess { cameraListData ->
                        _state.update {
                            it.copy(
                                availableCameraBrands = cameraListData.brands,
                                availableCameraTypes = cameraListData.types,
                                isCamerasLoading = false,
                                cameraLoadError = null,
                            )
                        }
                        Log.d("CameraLoad", "카메라 목록 로딩 성공: ${cameraListData.brands.size}개 브랜드")
                    }
                    .onFailure { error ->
                        _state.update {
                            it.copy(
                                isCamerasLoading = false,
                                cameraLoadError = "카메라 목록을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.",
                            )
                        }
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
                    _state.update { it.copy(showToast = false, toastMessage = null, toastMessageResId = null) }
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
                                    error = AppError.Auth.SocialInfoMissing,
                                    showToast = true,
                                    toastMessage = "로그인 정보를 찾지 못했습니다. 다시 로그인해 주세요.",
                                )
                            }
                            return@launch
                        }

                        val currentState = _state.value
                        if (currentState.userInfo.nickname.isNullOrBlank()) {
                            _state.update {
                                it.copy(
                                    isSubmitting = false,
                                    showToast = true,
                                    toastMessage = "닉네임 정보가 없어 가입을 진행할 수 없습니다.",
                                )
                            }
                            return@launch
                        }

                        if (currentState.selectedAreas.isEmpty()) {
                            _state.update {
                                it.copy(
                                    isSubmitting = false,
                                    showToast = true,
                                    toastMessage = "주 촬영지를 하나 이상 선택해 주세요.",
                                )
                            }
                            return@launch
                        }

                        if (currentState.phoneDevices.isEmpty() && currentState.cameraDevices.isEmpty()) {
                            _state.update {
                                it.copy(
                                    isSubmitting = false,
                                    showToast = true,
                                    toastMessage = "촬영 장비를 하나 이상 등록해 주세요.",
                                )
                            }
                            return@launch
                        }

                        val signup =
                            PhotographerSignup(
                                nickname = currentState.userInfo.nickname.orEmpty(),
                                socialEmail = tokenManager.getSocialEmail(),
                                socialProvider = tokenManager.getSocialProvider(),
                                socialCode = socialCode,
                                profileImage = currentState.userInfo.profileImageObjectKey,
                                photoMoods = currentState.selectedVibeChipList.map { it.label },
                                activeAreas =
                                    currentState.selectedAreas.mapIndexed { index, area ->
                                        PhotographerSignupActiveArea(
                                            code = area.id,
                                            priority = index + 1,
                                        )
                                    },
                                cameras =
                                    currentState.phoneDevices.map {
                                        PhotographerSignupCamera(
                                            type = PhotographerSignupCameraType.PHONE,
                                            brand = it.companyName,
                                            name = it.modelName,
                                            cameraType = null,
                                        )
                                    } +
                                        currentState.cameraDevices.map {
                                            PhotographerSignupCamera(
                                                type = PhotographerSignupCameraType.CAMERA,
                                                brand = it.companyName,
                                                name = it.modelName,
                                                cameraType = it.cameraType,
                                            )
                                        },
                            )

                        photographerService.createPhotographer(signup)
                            .onSuccess {
                                val user = currentState.userInfo
                                _state.update { it.copy(isSubmitting = false) }
                                _sideEffect.send(
                                    SignUpPhotographerSideEffect.NavigateToSignUpCompletion(user),
                                )
                            }
                            .onFailure { error ->
                                val appError = AppError.fromThrowable(error)
                                _state.update {
                                    it.copy(
                                        isSubmitting = false,
                                        error = appError,
                                        showToast = true,
                                        toastMessage = "가입을 완료하지 못했습니다. 잠시 후 다시 시도해 주세요.",
                                    )
                                }
                            }
                    }
                }

                is SignUpPhotographerIntent.SearchArea -> {
                    manualSearchRequests.tryEmit(intent.keyword)
                }

                is SignUpPhotographerIntent.UpdateSearchQuery -> {
                    _state.update {
                        it.copy(
                            searchQuery = intent.query,
                            searchError = null,
                            hasSearchCompleted = false,
                        )
                    }
                    autoSearchRequests.tryEmit(intent.query)
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

        private suspend fun executeSearchArea(keyword: String) {
            if (keyword.isBlank()) {
                _state.update {
                    it.copy(
                        hasSearchCompleted = false,
                        searchError = null,
                    )
                }
                loadNearbyAreasOnInit()
                return
            }

            _state.update {
                it.copy(
                    isSearching = true,
                    hasSearchCompleted = false,
                    searchError = null,
                )
            }

            addressService.searchArea(keyword)
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
                            isSearching = false,
                            hasSearchCompleted = true,
                            searchError = "검색 서버에 문제가 있어요. 잠시 후 다시 시도해 주세요.",
                        )
                    }
                    Log.e("AddressSearch", "지역 검색 실패: query=$keyword", error)
                }
        }
    }
