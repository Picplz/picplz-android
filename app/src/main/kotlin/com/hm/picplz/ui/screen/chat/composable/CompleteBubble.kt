package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.DeliveryType
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CompleteBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage
) {
    Surface(
        modifier = modifier
            .width(238.dp),
        shape = RoundedCornerShape(20.dp),
        color = when(chatMessage.direction) {
            MessageDirection.SENT -> MainThemeColor.Gray1
            MessageDirection.RECEIVED -> MainThemeColor.White
        },
        border = when(chatMessage.direction) {
            MessageDirection.SENT -> null
            MessageDirection.RECEIVED ->
                BorderStroke(
                    width = 1.dp,
                    color = MainThemeColor.Gray3
                )
        }
    ) {
        Column {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteBubblePreview() {
    PicplzTheme {
        CompleteBubble(
            chatMessage = ChatMessage(
                id = 1,
                content = MessageContent.Completion(
                    title = "상품명",
                    deliveryMethod = DeliveryType.EMAIL,
                    deliveryDeadline = System.currentTimeMillis() - 1000,
                ),
                direction = MessageDirection.RECEIVED,
                sender = User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 1000,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteBubbleReceivedPreview() {
    PicplzTheme {
        CompleteBubble(
            chatMessage = ChatMessage(
                id = 1,
                content = MessageContent.Completion(
                    title = "상품명",
                    deliveryMethod = DeliveryType.EMAIL,
                    deliveryDeadline = System.currentTimeMillis() - 1000,
                ),
                direction = MessageDirection.RECEIVED,
                sender = User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 1000,
            )
        )
    }
}