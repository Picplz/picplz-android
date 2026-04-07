package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.ui.screen.my_page.shootingHistoryCard.ShootingStatus

data class ShootingHistoryState(
    val shootingHistories: List<ShootingHistoryItem> = emptyList(),
    val isLoading: Boolean = false,
) {
    companion object {
        fun idle() = ShootingHistoryState()
    }
}

data class ShootingHistoryItem(
    val id: String,
    val photographerName: String,
    val photographerImageUri: String,
    val productName: String,
    val price: Int,
    val status: ShootingStatus,
    val paymentDate: String,
    val shootingDate: String,
    val shootingLocation: String,
    val hasChatRoom: Boolean,
)
