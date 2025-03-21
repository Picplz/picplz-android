package com.hm.picplz.ui.screen.photographer_main

data class PhotographerMainState (
    val isActive: Boolean = true
) {
    companion object {
        fun idle(): PhotographerMainState {
            return PhotographerMainState()
        }
    }
}