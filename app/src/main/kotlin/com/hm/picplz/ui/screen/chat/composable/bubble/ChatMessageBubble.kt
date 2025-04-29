package com.hm.picplz.ui.screen.chat.composable.bubble

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatMessageBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
){
    ChatBubbleSurface(
        modifier = modifier
            .widthIn(
                max = if (chatMessage.direction == MessageDirection.RECEIVED) 238.dp else 273.dp
            ),
        direction = chatMessage.direction
    ) {
        val messageContent = chatMessage.content as MessageContent.Text

        Text(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp,
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
                content = MessageContent.Text("어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다"),
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