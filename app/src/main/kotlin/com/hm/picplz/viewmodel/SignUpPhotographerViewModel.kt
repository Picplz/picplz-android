package com.hm.picplz.viewmodel

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.data.model.PhotographyExperience
import com.hm.picplz.data.service.AddressService
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.ui.model.Device
import com.hm.picplz.ui.model.DeviceCategory
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.CareerPeriod
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpPhotographerViewModel @Inject constructor(
    private val addressService: AddressService,
    private val locationService: LocationService
) : ViewModel() {
    private val _state = MutableStateFlow<SignUpPhotographerState>(SignUpPhotographerState.idle())
    val state: StateFlow<SignUpPhotographerState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpPhotographerSideEffect>()
    val sideEffect: SharedFlow<SignUpPhotographerSideEffect> get() = _sideEffect

    init {
        loadNearbyAreasOnInit()
    }

    private fun loadNearbyAreasOnInit() {
        viewModelScope.launch {
            locationService.getCurrentLocation(
                onLocationReceived = { location ->
                    _state.update { it.copy(hasLocationPermission = true) }

                    loadNearbyAreas(
                        lat = location.latitude,
                        lng = location.longitude
                    )
                },
                onPermissionDenied = {
                    Log.w("LocationInfo", "위치 권한 거부됨")

                    _state.update { it.copy(
                        hasLocationPermission = false,
                        searchResults = emptyList(),
                        isSearching = false,
                        searchError = "위치 권한이 필요합니다"
                    )}
                }
            )
        }
    }

    private fun loadNearbyAreas(lat: Double, lng: Double) {
        _state.update { it.copy(isSearching = true) }

        viewModelScope.launch {
            addressService.getNearbyAreas(3, lat, lng)
                .onSuccess { nearbyAreas ->
                    _state.update { it.copy(
                        searchResults = nearbyAreas,
                        isSearching = false,
                        searchError = null
                    )}
                    Log.d("AddressSearch", "근처 지역 로딩 성공: ${nearbyAreas.size}개")
                }
                .onFailure { error ->
                    _state.update { it.copy(
                        searchResults = emptyList(),
                        isSearching = false,
                        searchError = "근처 지역을 불러올 수 없습니다"
                    )}
                    Log.e("AddressSearch", "근처 지역 로딩 실패", error)
                }
        }
    }

    fun handleIntent(intent: SignUpPhotographerIntent) {
        when (intent) {
            is NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpPhotographerSideEffect.NavigateToPrev)
                }
            }
            is SetUserInfo -> {
                _state.update { it.copy(userInfo = intent.userInfo)}
            }
            is SetHasPhotographyExperience -> {
                val newPhotographyExperienceState = _state.value.copy(
                    hasPhotographyExperience = if (_state.value.hasPhotographyExperience == intent.hasExperience) {
                        null
                    } else {
                        intent.hasExperience
                    }
                )
                _state.value = newPhotographyExperienceState
            }
            is Navigate -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpPhotographerSideEffect.Navigate(intent.destination))
                }
            }
            is SetPhotographyExperience -> {
                _state.update { it.copy(selectedPhotographyExperienceId = intent.photographyExperienceId)}
            }
            is SetEditingChipId -> {
                _state.update { it.copy(editingChipId = intent.chipId)}
            }
            is AddVibeChip -> {
                val maxId = _state.value.vibeChipList
                    .maxByOrNull { it.id.toIntOrNull() ?: 0 }?.id?.toIntOrNull() ?: 0
                val newId = (maxId + 1).toString()

                val newChip = ChipItem(id = newId, label = intent.label, isEditable = true)
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList + newChip
                    currentState.copy(vibeChipList = updatedChipList)
                }
            }
            is DeleteVibeChip -> {
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList.filter { it.id != intent.chipId }
                    currentState.copy(vibeChipList = updatedChipList)
                }
            }
            is UpdateVibeChip -> {
                _state.update { currentState ->
                    val updatedChipList = currentState.vibeChipList.map { chip ->
                        if (chip.id == intent.chipId) {
                            chip.copy(label = intent.label)
                        } else {
                            chip
                        }
                    }
                    currentState.copy(vibeChipList = updatedChipList)
                }
            }
            is UpdateSelectedVibeChipList -> {
                _state.update { currentState ->
                    val updateSelectedChipList = if (currentState.selectedVibeChipList.any {it.id == intent.chipId}) {
                        currentState.selectedVibeChipList.filter { it.id != intent.chipId }
                    } else {
                        currentState.selectedVibeChipList + ChipItem(id = intent.chipId, label = intent.label)
                    }
                    currentState.copy(selectedVibeChipList = updateSelectedChipList)
                }
            }
            is SetUserPhotographyExperience -> {
                val experience = when (_state.value.selectedPhotographyExperienceId) {
                    "1" -> PhotographyExperience.PHOTO_MAJOR
                    "2" -> PhotographyExperience.INCOME_GENERATION
                    "3" -> PhotographyExperience.SNS_OPERATION
                    else -> null
                }

                experience?.let { newExperience ->
                    val updatedUser = _state.value.userInfo.copy(
                        photographyExperience = newExperience
                    )
                    _state.update { it.copy( userInfo = updatedUser ) }
                }
            }
            is NavigateWithSubmit -> {
                viewModelScope.launch {
                    val userBundle  = if (_state.value.vibeChipList.isNotEmpty()) {
                        bundleOf(
                            "userInfo" to _state.value.userInfo
                        )
                    } else bundleOf()
                    _sideEffect.emit(SignUpPhotographerSideEffect.NavigateWithSubmit(intent.destination, userBundle))
                }
            }
            is SetUserPhotographyVibe -> {
                val updatedUser = _state.value.userInfo.copy(
                    photographyVibes = _state.value.selectedVibeChipList.map { chip -> chip.label }
                )
                _state.update { it.copy(userInfo = updatedUser) }
            }
            is SetIsOpenDialog -> {
                _state.update { it.copy( showInfoDialog = intent.isOpen) }
            }
            is SetYearValue -> {
                _state.update { it.copy( yearValue = intent.year ) }
            }
            is SetMonthValue -> {
                _state.update { it.copy( monthValue = intent.month ) }
            }
            is SetCareerPeriod -> {
                _state.update {
                    it.copy(
                        careerPeriod = if (it.yearValue != null && it.monthValue != null) {
                            CareerPeriod(years = it.yearValue, months = it.monthValue)
                        } else null
                    )
                }
            }
            is InitializeCareerValues -> {
                _state.update { it.copy(
                    yearValue = null,
                    monthValue = null
                )}
            }
            is SetSelectedSelector -> {
                _state.update { it.copy( selectedSelector = intent.selectedSelector ) }
            }
            is UpdateSearchQuery -> {
                _state.update { it.copy(
                    searchQuery = intent.query,
                    searchError = null,
                    hasSearchCompleted = false
                )}
            }

            is SearchArea -> {
                viewModelScope.launch {
                    if (intent.keyword.isBlank()) {
                        _state.update { it.copy(
                            hasSearchCompleted = false,
                            searchResults = emptyList()
                        )}
                        loadNearbyAreasOnInit()
                        return@launch
                    }

                    _state.update { it.copy(
                        isSearching = true,
                        hasSearchCompleted = false
                    )}

                    addressService.searchArea(intent.keyword)
                        .onSuccess { searchedAreas ->
                            _state.update { it.copy(
                                searchResults = searchedAreas,
                                isSearching = false,
                                hasSearchCompleted = true,
                                searchError = null
                            )}
                        }
                        .onFailure { error ->
                            _state.update { it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                hasSearchCompleted = true,
                                searchError = "검색 중 오류가 발생했습니다"
                            )}
                            Log.e("AddressSearch", "지역 검색 실패", error)
                        }
                }
            }

            is ToggleAreaSelection -> {
                _state.update { currentState ->
                    val isAlreadySelected = currentState.selectedAreas.any { it.id == intent.area.id }

                    if (!isAlreadySelected && currentState.selectedAreas.size >= 10) {
                        return@update currentState.copy(
                            toastMessage = "활동 지역은 최대 10개까지 선택할 수 있습니다.",
                            showToast = true
                        )
                    }

                    val newSelectedAreas = if (isAlreadySelected) {
                        currentState.selectedAreas.filter { it.id != intent.area.id }
                    } else {
                        currentState.selectedAreas + intent.area
                    }

                    currentState.copy(selectedAreas = newSelectedAreas)
                }
            }

            is RemoveSelectedArea -> {
                _state.update { currentState ->
                    currentState.copy(
                        selectedAreas = currentState.selectedAreas.filter { it.id != intent.area.id }
                    )
                }
            }

            is ClearSearchResults -> {
                _state.update { it.copy(
                    searchResults = emptyList(),
                    searchError = null
                )}
            }
            is DismissToast -> {
                _state.update { it.copy(showToast = false, toastMessage = null) }
            }
            is AddDeviceToCategory -> {
                _state.update { currentState ->
                    when (intent.device) {
                        is Device.PhoneDevice -> currentState.copy(
                            phoneDevices = currentState.phoneDevices + intent.device
                        )
                        is Device.CameraDevice -> currentState.copy(
                            cameraDevices = currentState.cameraDevices + intent.device
                        )
                    }
                }
            }
            is RemoveDeviceFromCategory -> {
                _state.update { currentState ->
                    when (intent.device) {
                        is Device.PhoneDevice -> currentState.copy(
                            phoneDevices = currentState.phoneDevices.filter { it.id != intent.device.id }
                        )
                        is Device.CameraDevice -> currentState.copy(
                            cameraDevices = currentState.cameraDevices.filter { it.id != intent.device.id }
                        )
                    }
                }
            }
            is SetBrandExpanded -> {
                _state.update { it.copy(brandExpanded = intent.expanded) }
            }
            is SetModelExpanded -> {
                _state.update { it.copy(modelExpanded = intent.expanded) }
            }
            is SetCameraTypeExpanded -> {
                _state.update { it.copy(cameraTypeExpanded = intent.expanded) }
            }
            is SetPhoneDirectInputMode -> {
                _state.update { currentState ->
                    currentState.copy(
                        phoneBrandDirectInput = intent.brandMode,
                        phoneModelDirectInput = intent.modelMode,
                        currentPhone = if (intent.brandMode || intent.modelMode) {
                            Device.PhoneDevice(
                                companyName = "",
                                modelName = ""
                            )
                        } else currentState.currentPhone
                    )
                }
            }
            is SetCameraDirectInputMode -> {
                _state.update { currentState ->
                    currentState.copy(
                        cameraBrandDirectInput = intent.brandMode,
                        currentCamera = if (intent.brandMode) {
                            Device.CameraDevice(
                                companyName = "",
                                modelName = "",
                                cameraType = ""
                            )
                        } else currentState.currentCamera
                    )
                }
            }
            is UpdateCurrentPhone -> {
                _state.update { currentState ->
                    currentState.copy(currentPhone = intent.phone)
                }
            }
            is UpdateCurrentCamera -> {
                _state.update { currentState ->
                    currentState.copy(currentCamera = intent.camera)
                }
            }
            is AddCurrentDeviceToList -> {
                _state.update { currentState ->
                    when (intent.category) {
                        DeviceCategory.PHONE -> {
                            currentState.currentPhone?.let { phone ->
                                currentState.copy(
                                    phoneDevices = currentState.phoneDevices + phone,
                                    currentPhone = null,
                                    brandExpanded = false,
                                    modelExpanded = false,
                                    phoneBrandDirectInput = false,
                                    phoneModelDirectInput = false
                                )
                            } ?: currentState
                        }
                        DeviceCategory.CAMERA -> {
                            currentState.currentCamera?.let { camera ->
                                currentState.copy(
                                    cameraDevices = currentState.cameraDevices + camera,
                                    currentCamera = null,
                                    brandExpanded = false,
                                    modelExpanded = false,
                                    cameraTypeExpanded = false,
                                    cameraBrandDirectInput = false
                                )
                            } ?: currentState
                        }
                    }
                }
            }
            is ResetCurrentDevice -> {
                _state.update { currentState ->
                    when (intent.category) {
                        DeviceCategory.PHONE -> currentState.copy(
                            currentPhone = null,
                            brandExpanded = false,
                            modelExpanded = false,
                            phoneBrandDirectInput = false,
                            phoneModelDirectInput = false
                        )
                        DeviceCategory.CAMERA -> currentState.copy(
                            currentCamera = null,
                            brandExpanded = false,
                            modelExpanded = false,
                            cameraTypeExpanded = false,
                            cameraBrandDirectInput = false
                        )
                    }
                }
            }
            is SetModelDirectInput -> {
                _state.update { currentState ->
                    when (intent.category) {
                        DeviceCategory.PHONE -> currentState.copy(
                            phoneModelDirectInput = intent.enabled,
                            modelExpanded = false
                        )
                        DeviceCategory.CAMERA -> {
                            currentState
                        }
                    }
                }
            }
        }
    }
}