package com.hm.picplz.ui.screen.photographer_main

import com.hm.picplz.domain.model.Equipment

data class PhotographerMainState(
    val isActive: Boolean = false,
    val isModalOpen: Boolean = false,
    val equipmentList: List<Equipment> = dummyEquipmentList
) {
    companion object {
        private val dummyEquipmentList = listOf(
            Equipment(1,"내 폰", "아이폰 16 Pro Max", true),
            Equipment(2,"카메라", "소니 a7m4", true),
            Equipment(3,"카메라", "소니 a7m4", false),
            Equipment(4,"카메라", "소니 a7m4", false)
        )
        fun idle(): PhotographerMainState {
            return PhotographerMainState()
        }
    }
}