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
    OPEN_ORDER_FORM,
    FIND_ANOTHER_ARTIST,
    CONFIRM_ORDER,
    OPEN_URL
}

data class MessageButton(
    val text: String,
    val actionType: ButtonActionType,
    val actionPayload: String? = null
)

enum class NotificationType {
    POSITIVE, NEGATIVE
}

enum class DeliveryType {
    EMAIL
}

sealed class MessageContent {
    data class Text(val message: String) : MessageContent()
    data class Image(val imageUrl: String) : MessageContent()
    data class Notification(
        val title: String? = null,
        val subtitle: String? = null,
        val content: String,
        val caption: String? = null,
        val type: NotificationType,
        val button: MessageButton? = null
    ): MessageContent()
    data class Completion(
        val title: String,
        val deliveryMethod: DeliveryType,
        val deliveryDeadline: Long,
    ): MessageContent()
    data class ChangeTime(
        val newScheduledTime: Long,
    ): MessageContent()
}

data class ChatMessage(
    val id: Int,
    val direction: MessageDirection,
    val content: MessageContent,
    val timestamp: Long,
    val sender: User,
    val isRead: Boolean = false
)