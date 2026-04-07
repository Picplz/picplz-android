package com.hm.picplz.ui.screen.my_page

sealed interface ShootingHistoryIntent {
    data object NavigateBack : ShootingHistoryIntent

    data class NavigateToChat(val historyId: String) : ShootingHistoryIntent

    data class WriteReview(val historyId: String) : ShootingHistoryIntent

    data class ViewOrderDetail(val historyId: String) : ShootingHistoryIntent

    data class DeleteHistory(val historyId: String) : ShootingHistoryIntent
}
