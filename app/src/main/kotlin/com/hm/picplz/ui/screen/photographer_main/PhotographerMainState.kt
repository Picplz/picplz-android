package com.hm.picplz.ui.screen.photographer_main

data class PhotographerMainState (
    val isActive: Boolean = false,
    val isModalOpen: Boolean = false
) {
    companion object {
        fun idle(): PhotographerMainState {
            return PhotographerMainState()
        }
    }
}