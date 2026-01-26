package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler

import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.AddCurrentDeviceToList
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.AddDeviceToCategory
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.RemoveDeviceFromCategory
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.ResetCurrentDevice
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetBrandExpanded
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetCameraDirectInputMode
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetCameraTypeExpanded
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetModelDirectInput
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetModelExpanded
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetPhoneDirectInputMode
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateCurrentCamera
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateCurrentPhone
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState
import java.util.UUID

class DeviceHandler {
    fun handleIntent(
        intent: SignUpPhotographerIntent,
        currentState: SignUpPhotographerState,
    ): SignUpPhotographerState? {
        return when (intent) {
            is AddDeviceToCategory -> {
                when (intent.device) {
                    is Device.PhoneDevice ->
                        currentState.copy(
                            phoneDevices = currentState.phoneDevices + intent.device,
                        )

                    is Device.CameraDevice ->
                        currentState.copy(
                            cameraDevices = currentState.cameraDevices + intent.device,
                        )
                }
            }

            is RemoveDeviceFromCategory -> {
                when (intent.device) {
                    is Device.PhoneDevice ->
                        currentState.copy(
                            phoneDevices = currentState.phoneDevices.filter { it.id != intent.device.id },
                        )

                    is Device.CameraDevice ->
                        currentState.copy(
                            cameraDevices = currentState.cameraDevices.filter { it.id != intent.device.id },
                        )
                }
            }

            is SetBrandExpanded -> {
                currentState.copy(brandExpanded = intent.expanded)
            }

            is SetModelExpanded -> {
                currentState.copy(modelExpanded = intent.expanded)
            }

            is SetCameraTypeExpanded -> {
                currentState.copy(cameraTypeExpanded = intent.expanded)
            }

            is SetPhoneDirectInputMode -> {
                currentState.copy(
                    phoneBrandDirectInput = intent.brandMode,
                    phoneModelDirectInput = intent.modelMode,
                    currentPhone =
                        if (intent.brandMode || intent.modelMode) {
                            Device.PhoneDevice(
                                id = UUID.randomUUID().toString(),
                                companyName = "",
                                modelName = "",
                            )
                        } else {
                            currentState.currentPhone
                        },
                )
            }

            is SetCameraDirectInputMode -> {
                currentState.copy(
                    cameraBrandDirectInput = intent.brandMode,
                    currentCamera =
                        if (intent.brandMode) {
                            Device.CameraDevice(
                                id = currentState.currentCamera?.id ?: UUID.randomUUID().toString(),
                                companyName = "",
                                modelName = currentState.currentCamera?.modelName ?: "",
                                cameraType = currentState.currentCamera?.cameraType ?: "",
                            )
                        } else {
                            currentState.currentCamera
                        },
                )
            }

            is UpdateCurrentPhone -> {
                currentState.copy(currentPhone = intent.phone)
            }

            is UpdateCurrentCamera -> {
                currentState.copy(currentCamera = intent.camera)
            }

            is AddCurrentDeviceToList -> {
                when (intent.category) {
                    DeviceCategory.PHONE -> {
                        currentState.currentPhone?.let { phone ->
                            val isDuplicate =
                                currentState.phoneDevices.any {
                                    it.companyName == phone.companyName && it.modelName == phone.modelName
                                }
                            if (isDuplicate) {
                                currentState.copy(
                                    showToast = true,
                                    toastMessage = "이미 추가된 기기입니다.",
                                )
                            } else {
                                currentState.copy(
                                    phoneDevices = currentState.phoneDevices + phone,
                                    currentPhone = null,
                                    brandExpanded = false,
                                    modelExpanded = false,
                                    phoneBrandDirectInput = false,
                                    phoneModelDirectInput = false,
                                )
                            }
                        } ?: currentState
                    }

                    DeviceCategory.CAMERA -> {
                        currentState.currentCamera?.let { camera ->
                            val isDuplicate =
                                currentState.cameraDevices.any {
                                    it.companyName == camera.companyName && it.modelName == camera.modelName
                                }
                            if (isDuplicate) {
                                currentState.copy(
                                    showToast = true,
                                    toastMessage = "이미 추가된 기기입니다.",
                                )
                            } else {
                                currentState.copy(
                                    cameraDevices = currentState.cameraDevices + camera,
                                    currentCamera = null,
                                    brandExpanded = false,
                                    modelExpanded = false,
                                    cameraTypeExpanded = false,
                                    cameraBrandDirectInput = false,
                                )
                            }
                        } ?: currentState
                    }
                }
            }

            is ResetCurrentDevice -> {
                when (intent.category) {
                    DeviceCategory.PHONE ->
                        currentState.copy(
                            currentPhone = null,
                            brandExpanded = false,
                            modelExpanded = false,
                            phoneBrandDirectInput = false,
                            phoneModelDirectInput = false,
                        )

                    DeviceCategory.CAMERA ->
                        currentState.copy(
                            currentCamera = null,
                            brandExpanded = false,
                            modelExpanded = false,
                            cameraTypeExpanded = false,
                            cameraBrandDirectInput = false,
                        )
                }
            }

            is SetModelDirectInput -> {
                when (intent.category) {
                    DeviceCategory.PHONE ->
                        currentState.copy(
                            phoneModelDirectInput = intent.enabled,
                            modelExpanded = false,
                        )

                    DeviceCategory.CAMERA -> {
                        currentState
                    }
                }
            }

            else -> null
        }
    }
}
