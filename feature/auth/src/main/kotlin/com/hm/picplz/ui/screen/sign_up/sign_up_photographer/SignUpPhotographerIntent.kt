package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.common.model.User
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.navigation.model.NavigationRoute

sealed interface SignUpPhotographerIntent {
    data class SetUserInfo(val userInfo: User) : SignUpPhotographerIntent

    data object NavigateToPrev : SignUpPhotographerIntent

    data class SetHasPhotographyExperience(val hasExperience: Boolean) : SignUpPhotographerIntent

    data class SetPhotographyExperience(val photographyExperienceId: String?) : SignUpPhotographerIntent

    data class Navigate(val destination: NavigationRoute) : SignUpPhotographerIntent

    data class SetEditingChipId(val chipId: String?) : SignUpPhotographerIntent

    data class AddVibeChip(val label: String) : SignUpPhotographerIntent

    data class DeleteVibeChip(val chipId: String) : SignUpPhotographerIntent

    data class UpdateVibeChip(val chipId: String, val label: String) : SignUpPhotographerIntent

    data class UpdateSelectedVibeChipList(val chipId: String, val label: String) : SignUpPhotographerIntent

    data object SetUserPhotographyExperience : SignUpPhotographerIntent

    data object NavigateWithSubmit : SignUpPhotographerIntent

    data object SetUserPhotographyVibe : SignUpPhotographerIntent

    data class SetIsOpenDialog(val isOpen: Boolean) : SignUpPhotographerIntent

    data class SetYearValue(val year: Int) : SignUpPhotographerIntent

    data class SetMonthValue(val month: Int) : SignUpPhotographerIntent

    data object SetCareerPeriod : SignUpPhotographerIntent

    data class SetSelectedSelector(val selectedSelector: SelectorType) : SignUpPhotographerIntent

    data class SearchArea(val keyword: String) : SignUpPhotographerIntent

    data class ToggleAreaSelection(val area: Area) : SignUpPhotographerIntent

    data class RemoveSelectedArea(val area: Area) : SignUpPhotographerIntent

    data object ClearSearchResults : SignUpPhotographerIntent

    data class UpdateSearchQuery(val query: String) : SignUpPhotographerIntent

    data object DismissToast : SignUpPhotographerIntent

    data class AddDeviceToCategory(val device: Device) : SignUpPhotographerIntent

    data class RemoveDeviceFromCategory(val device: Device) : SignUpPhotographerIntent

    data class UpdateCurrentPhone(val phone: Device.PhoneDevice?) : SignUpPhotographerIntent

    data class UpdateCurrentCamera(val camera: Device.CameraDevice?) : SignUpPhotographerIntent

    data class ResetCurrentDevice(val category: DeviceCategory) : SignUpPhotographerIntent

    data class SetBrandExpanded(val expanded: Boolean) : SignUpPhotographerIntent

    data class SetModelExpanded(val expanded: Boolean) : SignUpPhotographerIntent

    data class SetCameraTypeExpanded(val expanded: Boolean) : SignUpPhotographerIntent

    data class SetPhoneDirectInputMode(val brandMode: Boolean, val modelMode: Boolean) : SignUpPhotographerIntent

    data class SetCameraDirectInputMode(val brandMode: Boolean) : SignUpPhotographerIntent

    data class SetModelDirectInput(val category: DeviceCategory, val enabled: Boolean) : SignUpPhotographerIntent

    data class AddCurrentDeviceToList(val category: DeviceCategory) : SignUpPhotographerIntent
}
