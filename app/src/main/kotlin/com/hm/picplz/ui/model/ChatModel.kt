package com.hm.picplz.ui.model
import com.hm.picplz.data.model.User

enum class ChatStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    COMPLETED
}

data class ChatRoomInfo(
    val id: String,
    val chatStatus: ChatStatus,
    val packageType: String,
    val lastMessage: Message,
    val unreadMessageCount: Int = 0,
)

data class Message (
    val profileImageUrl: String,
    val nickname: String,
    val message: String,
    val sentAt: Long = System.currentTimeMillis(),
)

enum class MessageDirection {
    SENT,
    RECEIVED
}

enum class ButtonActionType {
    OPEN_URL,
    CANCEL,
    CONFIRM,
}

data class MessageButton(
    val text: String,
    val actionType: ButtonActionType,
    val actionPayload: String? = null,
)

sealed class MessageContent {
    data class Text(val message: String) : MessageContent()
    data class Image(val imageUrl: String) : MessageContent()
    data class Notification(
        val message: String,
        val button: MessageButton? = null
    ): MessageContent()
}

data class ChatMessage(
    val id: Int,
    val direction: MessageDirection,
    val content: MessageContent,
    val timestamp: String,
    val sender: User,
    val isRead: Boolean = false
)