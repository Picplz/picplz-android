package com.hm.picplz.ui.screen.photographer_main

import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.navigation.model.NavigationRoute

sealed interface PhotographerMainIntent {
    data object NavigateToPrev : PhotographerMainIntent

    data class Navigate(val destination: NavigationRoute) : PhotographerMainIntent

    data class SetIsModalOpen(val isModalOpen: Boolean) : PhotographerMainIntent

    data class SetIsActive(val isActive: Boolean) : PhotographerMainIntent

    data class ToggleEquipmentEnabled(val deviceId: String) : PhotographerMainIntent

    data class RemoveDeviceFromCategory(val device: Device) : PhotographerMainIntent

    data class UpdateCurrentPhone(val phone: Device.PhoneDevice?) : PhotographerMainIntent

    data class UpdateCurrentCamera(val camera: Device.CameraDevice?) : PhotographerMainIntent

    data class ResetCurrentDevice(val category: DeviceCategory) : PhotographerMainIntent

    data class SetBrandExpanded(val expanded: Boolean) : PhotographerMainIntent

    data class SetModelExpanded(val expanded: Boolean) : PhotographerMainIntent

    data class SetCameraTypeExpanded(val expanded: Boolean) : PhotographerMainIntent

    data class SetPhoneDirectInputMode(val brandMode: Boolean, val modelMode: Boolean) : PhotographerMainIntent

    data class SetCameraDirectInputMode(val brandMode: Boolean) : PhotographerMainIntent

    data class SetModelDirectInput(val category: DeviceCategory, val enabled: Boolean) : PhotographerMainIntent

    data class AddCurrentDeviceToList(val category: DeviceCategory) : PhotographerMainIntent

    data object DismissToast : PhotographerMainIntent
}
