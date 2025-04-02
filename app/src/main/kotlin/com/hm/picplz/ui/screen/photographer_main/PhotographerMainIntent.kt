package com.hm.picplz.ui.screen.photographer_main

sealed class PhotographerMainIntent {
    data class SetModalState(val isModalOpen: Boolean) : PhotographerMainIntent()
}