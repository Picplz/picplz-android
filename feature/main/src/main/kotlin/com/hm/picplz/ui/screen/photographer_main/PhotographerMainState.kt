package com.hm.picplz.ui.screen.photographer_main

import com.hm.picplz.domain.model.CameraBrand
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.Equipment

data class PhotographerMainState(
    val isActive: Boolean = false,
    val isModalOpen: Boolean = false,
    val phoneDevices: List<Device.PhoneDevice> = dummyPhoneDevices,
    val cameraDevices: List<Device.CameraDevice> = dummyCameraDevices,
    val enabledEquipmentIds: Set<String> = dummyEnabledEquipmentIds,
    val currentPhone: Device.PhoneDevice? = null,
    val currentCamera: Device.CameraDevice? = null,
    val phoneBrandDirectInput: Boolean = false,
    val phoneModelDirectInput: Boolean = false,
    val cameraBrandDirectInput: Boolean = false,
    val brandExpanded: Boolean = false,
    val modelExpanded: Boolean = false,
    val cameraTypeExpanded: Boolean = false,
    val showDuplicateDeviceToast: Boolean = false,
    val availableCameraBrands: List<CameraBrand> = emptyList(),
    val availableCameraTypes: List<String> = emptyList(),
    val isCameraCatalogLoading: Boolean = false,
    val cameraCatalogLoadError: Throwable? = null,
) {
    val equipmentList: List<Equipment>
        get() = phoneDevices.map { it.toEquipment() } + cameraDevices.map { it.toEquipment() }

    companion object {
        private val dummyPhoneDevices =
            listOf(
                Device.PhoneDevice(
                    id = "phone-1",
                    companyName = "애플",
                    modelName = "아이폰 16 Pro Max",
                ),
            )
        private val dummyCameraDevices =
            listOf(
                Device.CameraDevice(
                    id = "camera-1",
                    companyName = "소니",
                    modelName = "a7m4",
                    cameraType = "미러리스 카메라",
                ),
            )
        private val dummyEnabledEquipmentIds =
            (dummyPhoneDevices.map { it.id } + dummyCameraDevices.map { it.id }).toSet()

        fun idle(): PhotographerMainState {
            return PhotographerMainState()
        }
    }

    private fun Device.PhoneDevice.toEquipment(): Equipment {
        return Equipment(
            id = id,
            type = "내 폰",
            deviceName = modelName.orEmpty(),
            isEnabled = id in enabledEquipmentIds,
        )
    }

    private fun Device.CameraDevice.toEquipment(): Equipment {
        return Equipment(
            id = id,
            type = "카메라",
            deviceName = listOfNotNull(modelName, cameraType?.let { "($it)" }).joinToString(" "),
            isEnabled = id in enabledEquipmentIds,
        )
    }
}
