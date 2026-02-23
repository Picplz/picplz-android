package com.hm.picplz.ui.screen.quick_shoot

sealed interface QuickShootSideEffect {
    data object NavigateToPrev : QuickShootSideEffect

    data object RequestLocationPermission : QuickShootSideEffect
}
