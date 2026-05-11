package com.hm.picplz.domain.model

import com.hm.picplz.common.model.User

enum class ChatStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    COMPLETED,
}

data class ChatRoomInfo(
    val id: String,
    val chatStatus: ChatStatus,
    val packageType: String,
    val lastMessage: Message,
    val unreadMessageCount: Int = 0,
    val isAlarmOn: Boolean = true,
)

data class Message(
    val profileImageUrl: String,
    val nickname: String,
    val message: String,
    val sentAt: Long = System.currentTimeMillis(),
)

enum class MessageDirection {
    SENT,
    RECEIVED,
}

sealed interface ButtonActionType {
    data object OpenOrderForm : ButtonActionType

    data object FindAnotherArtist : ButtonActionType

    data object ConfirmOrder : ButtonActionType

    data class OpenUrl(
        val url: String,
    ) : ButtonActionType

    data object OpenPhotographerDetailReservation : ButtonActionType
}

data class MessageButton(
    val text: String,
    val actionType: ButtonActionType,
    val actionPayload: String? = null,
)

enum class NotificationType {
    POSITIVE,
    NEGATIVE,
}

enum class DeliveryType {
    EMAIL,
}

sealed class MessageContent {
    data class Text(val message: String) : MessageContent()

    data class Image(val imageUris: List<String>) : MessageContent()

    data class Notification(
        val title: String? = null,
        val subtitle: String? = null,
        val content: String,
        val caption: String? = null,
        val type: NotificationType,
        val button: MessageButton? = null,
    ) : MessageContent()

    data class Completion(
        val title: String,
        val deliveryMethod: DeliveryType,
        val deliveryDeadline: Long,
    ) : MessageContent()

    data class ChangeTime(
        val newScheduledTime: Long,
    ) : MessageContent()

    data class DealConfirmation(
        val button: MessageButton? = null,
    ) : MessageContent()

    data class ChatSuggest(
        val suggestedChats: List<String>,
    ) : MessageContent()
}

data class ChatMessage(
    val id: Int,
    val direction: MessageDirection,
    val content: MessageContent,
    val timestamp: Long,
    val sender: User,
    val receiver: User,
    val isRead: Boolean = false,
)
