package com.hm.picplz.ui.screen.photographer_main

sealed class PhotographerMainIntent {
    data class SetIsModalOpen(val isModalOpen: Boolean) : PhotographerMainIntent()
    data class SetIsActive(val isActive: Boolean) : PhotographerMainIntent()
}