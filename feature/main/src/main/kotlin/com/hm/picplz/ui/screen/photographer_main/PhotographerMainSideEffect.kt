package com.hm.picplz.ui.screen.photographer_main

import com.hm.picplz.navigation.model.NavigationRoute

sealed interface PhotographerMainSideEffect {
    data object NavigateToPrev : PhotographerMainSideEffect

    data class Navigate(val destination: NavigationRoute) : PhotographerMainSideEffect
}
