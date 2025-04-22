package com.hm.picplz.ui.screen.chat.composable

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hm.picplz.R
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
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (chatMessage.direction === MessageDirection.RECEIVED) Arrangement.Start else Arrangement.End
    ) {
        if (chatMessage.direction === MessageDirection.RECEIVED) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(chatMessage.sender.profileImageUri ?: R.drawable.active_dot)
                    .crossfade(true)
                    .placeholder(R.drawable.active_dot)
                    .error(R.drawable.active_dot)
                    .build(),
                contentDescription = "메세지 프로필",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Surface(
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
                        max = 253.dp
                    )
                ,
                text = messageContent.message,
                style = caption,
            )
        }
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
                timestamp = (System.currentTimeMillis() - 100000).toString()
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
                content = MessageContent.Text("말풍선 어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다"),
                direction = MessageDirection.RECEIVED,
                sender = User(
                    id = "2",
                    nickname = "유가영 작가",
                    profileImageUri = Uri.parse(
                        "https://pbs.twimg.com/media/GlRFZh2akAA6KLR?format=jpg&name=large"
                    )
                ),
                timestamp = (System.currentTimeMillis() - 100000).toString()
            ),
        )
    }
}