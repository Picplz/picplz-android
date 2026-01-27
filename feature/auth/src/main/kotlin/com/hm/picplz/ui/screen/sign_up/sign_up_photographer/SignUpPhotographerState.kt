package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.ChipItem
import com.hm.picplz.common.model.User
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.Device

enum class SelectorType {
    NONE,
    YEAR,
    MONTH,
}

data class CareerPeriod(
    val years: Int = 0,
    val months: Int = 0,
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
                ChipItem(id = "1", label = "캐주얼"),
                ChipItem(id = "2", label = "고급미"),
                ChipItem(id = "3", label = "심플"),
                ChipItem(id = "4", label = "단아"),
                ChipItem(id = "5", label = "몽환적"),
                ChipItem(id = "6", label = "빈티지"),
                ChipItem(id = "7", label = "청량"),
                ChipItem(id = "8", label = "화려"),
                ChipItem(id = "9", label = "퇴폐적"),
                ChipItem(id = "10", label = "키치"),
                ChipItem(id = "11", label = "힙스터"),
            )
        }

        fun idle(): SignUpPhotographerState {
            return SignUpPhotographerState()
        }
    }
}
