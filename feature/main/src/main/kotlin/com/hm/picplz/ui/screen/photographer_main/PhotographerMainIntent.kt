package com.hm.picplz.ui.screen.photographer_main

import com.hm.picplz.navigation.model.NavigationRoute

sealed interface PhotographerMainIntent {
    data object NavigateToPrev : PhotographerMainIntent

    data class Navigate(val destination: NavigationRoute) : PhotographerMainIntent

    data class SetIsModalOpen(val isModalOpen: Boolean) : PhotographerMainIntent

    data class SetIsActive(val isActive: Boolean) : PhotographerMainIntent

    data class ToggleEquipmentEnabled(val deviceId: Int) : PhotographerMainIntent
}
