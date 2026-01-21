package com.hm.picplz.ui.screen.photographer_main

sealed interface PhotographerMainIntent {
    data object NavigateToPrev : PhotographerMainIntent

    data class Navigate(val destination: Any) : PhotographerMainIntent

    data class SetIsModalOpen(val isModalOpen: Boolean) : PhotographerMainIntent

    data class SetIsActive(val isActive: Boolean) : PhotographerMainIntent

    data class ToggleEquipmentEnabled(val deviceId: Int) : PhotographerMainIntent
}
