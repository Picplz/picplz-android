package com.hm.picplz.ui.screen.photographer_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.domain.usecase.GetCameraCatalogUseCase
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
    constructor(
        private val getCameraCatalogUseCase: GetCameraCatalogUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(PhotographerMainState.idle())
        val state: StateFlow<PhotographerMainState> get() = _state

        private val _sideEffect = Channel<PhotographerMainSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            loadCameraCatalog()
        }

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
                        val updatedEnabledEquipmentIds =
                            if (intent.deviceId in it.enabledEquipmentIds) {
                                it.enabledEquipmentIds - intent.deviceId
                            } else {
                                it.enabledEquipmentIds + intent.deviceId
                            }
                        it.copy(
                            enabledEquipmentIds = updatedEnabledEquipmentIds,
                        )
                    }
                }
                is PhotographerMainIntent.RemoveDeviceFromCategory -> {
                    _state.update {
                        when (intent.device) {
                            is Device.PhoneDevice ->
                                it.copy(
                                    phoneDevices =
                                        it.phoneDevices.filterNot { device ->
                                            device.id == intent.device.id
                                        },
                                    enabledEquipmentIds = it.enabledEquipmentIds - intent.device.id,
                                )
                            is Device.CameraDevice ->
                                it.copy(
                                    cameraDevices =
                                        it.cameraDevices.filterNot { device ->
                                            device.id == intent.device.id
                                        },
                                    enabledEquipmentIds = it.enabledEquipmentIds - intent.device.id,
                                )
                        }
                    }
                }
                is PhotographerMainIntent.UpdateCurrentPhone -> {
                    _state.update { it.copy(currentPhone = intent.phone) }
                }
                is PhotographerMainIntent.UpdateCurrentCamera -> {
                    _state.update { it.copy(currentCamera = intent.camera) }
                }
                is PhotographerMainIntent.ResetCurrentDevice -> {
                    _state.update { it.resetCurrentDevice(intent.category) }
                }
                is PhotographerMainIntent.SetBrandExpanded -> {
                    _state.update { it.copy(brandExpanded = intent.expanded) }
                }
                is PhotographerMainIntent.SetModelExpanded -> {
                    _state.update { it.copy(modelExpanded = intent.expanded) }
                }
                is PhotographerMainIntent.SetCameraTypeExpanded -> {
                    _state.update { it.copy(cameraTypeExpanded = intent.expanded) }
                }
                is PhotographerMainIntent.SetPhoneDirectInputMode -> {
                    _state.update {
                        it.copy(
                            phoneBrandDirectInput = intent.brandMode,
                            phoneModelDirectInput = intent.modelMode,
                            currentPhone =
                                if (intent.brandMode || intent.modelMode) {
                                    Device.PhoneDevice(
                                        companyName = "",
                                        modelName = "",
                                    )
                                } else {
                                    it.currentPhone
                                },
                        )
                    }
                }
                is PhotographerMainIntent.SetCameraDirectInputMode -> {
                    _state.update {
                        it.copy(
                            cameraBrandDirectInput = intent.brandMode,
                            currentCamera =
                                if (intent.brandMode) {
                                    Device.CameraDevice(
                                        id = it.currentCamera?.id ?: Device.CameraDevice(companyName = "").id,
                                        companyName = "",
                                        modelName = it.currentCamera?.modelName ?: "",
                                        cameraType = it.currentCamera?.cameraType ?: "",
                                    )
                                } else {
                                    it.currentCamera
                                },
                        )
                    }
                }
                is PhotographerMainIntent.SetModelDirectInput -> {
                    _state.update {
                        when (intent.category) {
                            DeviceCategory.PHONE ->
                                it.copy(
                                    phoneModelDirectInput = intent.enabled,
                                    modelExpanded = false,
                                )
                            DeviceCategory.CAMERA -> it
                        }
                    }
                }
                is PhotographerMainIntent.AddCurrentDeviceToList -> {
                    addCurrentDeviceToList(intent.category)
                }
                is PhotographerMainIntent.DismissToast -> {
                    _state.update { it.copy(showDuplicateDeviceToast = false) }
                }
            }
        }

        private fun loadCameraCatalog() {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isCameraCatalogLoading = true,
                        cameraCatalogLoadError = null,
                    )
                }
                getCameraCatalogUseCase()
                    .onSuccess { cameraCatalog ->
                        _state.update {
                            it.copy(
                                availableCameraBrands = cameraCatalog.brands,
                                availableCameraTypes = cameraCatalog.types,
                                isCameraCatalogLoading = false,
                            )
                        }
                    }
                    .onFailure { throwable ->
                        _state.update {
                            it.copy(
                                isCameraCatalogLoading = false,
                                cameraCatalogLoadError = throwable,
                            )
                        }
                    }
            }
        }

        private fun addCurrentDeviceToList(category: DeviceCategory) {
            val currentState = _state.value
            val newState =
                when (category) {
                    DeviceCategory.PHONE -> currentState.addCurrentPhone()
                    DeviceCategory.CAMERA -> currentState.addCurrentCamera()
                }
            _state.update { newState }
            if (!newState.showDuplicateDeviceToast) {
                viewModelScope.launch {
                    _sideEffect.send(PhotographerMainSideEffect.NavigateToPrev)
                }
            }
        }

        private fun PhotographerMainState.addCurrentPhone(): PhotographerMainState {
            val phone = currentPhone ?: return this
            val isDuplicate =
                phoneDevices.any {
                    it.companyName == phone.companyName && it.modelName == phone.modelName
                }
            if (isDuplicate) {
                return copy(showDuplicateDeviceToast = true)
            }
            return copy(
                phoneDevices = phoneDevices + phone,
                enabledEquipmentIds = enabledEquipmentIds + phone.id,
                currentPhone = null,
                brandExpanded = false,
                modelExpanded = false,
                phoneBrandDirectInput = false,
                phoneModelDirectInput = false,
                showDuplicateDeviceToast = false,
            )
        }

        private fun PhotographerMainState.addCurrentCamera(): PhotographerMainState {
            val camera = currentCamera ?: return this
            val isDuplicate =
                cameraDevices.any {
                    it.companyName == camera.companyName && it.modelName == camera.modelName
                }
            if (isDuplicate) {
                return copy(showDuplicateDeviceToast = true)
            }
            return copy(
                cameraDevices = cameraDevices + camera,
                enabledEquipmentIds = enabledEquipmentIds + camera.id,
                currentCamera = null,
                brandExpanded = false,
                modelExpanded = false,
                cameraTypeExpanded = false,
                cameraBrandDirectInput = false,
                showDuplicateDeviceToast = false,
            )
        }

        private fun PhotographerMainState.resetCurrentDevice(category: DeviceCategory): PhotographerMainState {
            return when (category) {
                DeviceCategory.PHONE ->
                    copy(
                        currentPhone = null,
                        brandExpanded = false,
                        modelExpanded = false,
                        phoneBrandDirectInput = false,
                        phoneModelDirectInput = false,
                        showDuplicateDeviceToast = false,
                    )
                DeviceCategory.CAMERA ->
                    copy(
                        currentCamera = null,
                        brandExpanded = false,
                        modelExpanded = false,
                        cameraTypeExpanded = false,
                        cameraBrandDirectInput = false,
                        showDuplicateDeviceToast = false,
                    )
            }
        }
    }
