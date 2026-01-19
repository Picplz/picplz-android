package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.data.model.ChipItem
import com.hm.picplz.data.model.User
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.mockdata.emptyUserData

enum class SelectorType {
    NONE,
    YEAR,
    MONTH
}

data class CareerPeriod(
    val years: Int = 0,
    val months: Int = 0
)

data class SignUpPhotographerState(
    val currentStep: Int? = 0,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val userInfo: User = emptyUserData,
    val hasPhotographyExperience: Boolean? = null,
    val selectedPhotographyExperienceId: String? = null,
    val experienceChipList: List<ChipItem> = defaultExperienceChipList(),
    val vibeChipList: List<ChipItem> = defaultVibeChipList(),
    val selectedVibeChipList: List<ChipItem> = listOf(),
    val editingChipId: String? = null,
    val showInfoDialog: Boolean = false,
    val yearValue: Int? = null,
    val monthValue: Int? = null,
    val careerPeriod: CareerPeriod? = null,
    val selectedSelector: SelectorType = SelectorType.NONE,
    val searchQuery: String = "",
    val hasLocationPermission: Boolean? = null,
    val searchResults: List<Area> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearchCompleted: Boolean = false,
    val selectedAreas: List<Area> = emptyList(),
    val searchError: String? = null,
    val toastMessage: String? = null,
    val showToast: Boolean = false,
    val phoneDevices: List<Device.PhoneDevice> = emptyList(),
    val cameraDevices: List<Device.CameraDevice> = emptyList(),
    val currentPhone: Device.PhoneDevice? = null,
    val currentCamera: Device.CameraDevice? = null,
    val phoneBrandDirectInput: Boolean = false,
    val phoneModelDirectInput: Boolean = false,
    val cameraBrandDirectInput: Boolean = false,
    val brandExpanded: Boolean = false,
    val modelExpanded: Boolean = false,
    val cameraTypeExpanded: Boolean = false,
) {
    companion object {
        private fun defaultExperienceChipList(): List<ChipItem> {
            return listOf(
                ChipItem(id = "1", label = "사진 전공"),
                ChipItem(id = "2", label = "수익 창출"),
                ChipItem(id = "3", label = "SNS 계정 운영"),
            )
        }

        private fun defaultVibeChipList(): List<ChipItem> {
            return listOf(
                ChipItem(id = "1", label = "MZ 감성"),
                ChipItem(id = "2", label = "을지로 감성"),
                ChipItem(id = "3", label = "키치 감성"),
                ChipItem(id = "4", label = "퇴폐 감성"),
                ChipItem(id = "5", label = "올드머니 감성")
            )
        }

        fun idle(): SignUpPhotographerState {
            return SignUpPhotographerState()
        }
    }
}
