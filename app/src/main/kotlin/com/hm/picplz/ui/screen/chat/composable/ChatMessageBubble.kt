package com.hm.picplz.ui.screen.chat.composable

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatMessageBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
){
    Surface(
        modifier = modifier,
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
        val messageContent = chatMessage.content as MessageContent.Text

        Text(
            modifier = Modifier
                .padding(
                    horizontal = 11.dp,
                    vertical = 10.dp,
                )
                .widthIn(
                    max = if (chatMessage.direction == MessageDirection.RECEIVED) 218.dp else 253.dp
                )
            ,
            text = messageContent.message,
            style = caption,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageSentBubblePreview() {
    PicplzTheme {
        ChatMessageBubble(
            chatMessage = ChatMessage(
                id = 1,
                content = MessageContent.Text("말풍선 어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다"),
                direction = MessageDirection.SENT,
                sender = User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 100000
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageReceivedBubblePreview() {
    PicplzTheme {
        ChatMessageBubble(
            chatMessage = ChatMessage(
                id = 2,
                content = MessageContent.Text("말풍선 오는쪽 가로 최대 크기는 이만큼 입니다"),
                direction = MessageDirection.RECEIVED,
                sender = User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = Uri.parse(
                        "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large"
                    )
                ),
                timestamp = System.currentTimeMillis() - 100000
            ),
        )
    }
}