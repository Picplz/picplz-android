package com.hm.picplz.ui.screen.photographer_main


sealed class PhotographerMainSideEffect {
    data object NavigateToPrev : PhotographerMainSideEffect()
    data class Navigate(val destination: String) : PhotographerMainSideEffect()
}