package com.hm.picplz.ui.screen.my_page

sealed interface ShootingHistorySideEffect {
    data object NavigateBack : ShootingHistorySideEffect

    data class ShowToast(val message: String) : ShootingHistorySideEffect

    data class NavigateToOrderDetail(val historyId: String) : ShootingHistorySideEffect
}
