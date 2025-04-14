package com.hm.picplz.ui.model

enum class ChatStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    COMPLETED
}

data class ChatRoomInfo(
    val chatStatus: ChatStatus,
    val packageType: String,
    val lastMessage: Message,
)

data class Message (
    val profileImageUrl: String,
    val nickname: String,
    val message: String,
    val sentAt: Long = System.currentTimeMillis(),
)