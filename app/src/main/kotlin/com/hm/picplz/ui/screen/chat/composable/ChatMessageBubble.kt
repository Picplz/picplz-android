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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hm.picplz.R
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.utils.DateTimeUtil

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
        if (chatMessage.direction == MessageDirection.RECEIVED) {
            ChatMessageProfile(
                profileImageUri = chatMessage.sender.profileImageUri
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            val timeFontStyle = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 8.sp,
                lineHeight = 8.sp * 1.4,
                letterSpacing = 0.sp,
                color = MainThemeColor.Gray3
            )
            if (chatMessage.direction == MessageDirection.SENT) {
                Text(
                    text = DateTimeUtil.getFormattedTime(chatMessage.timestamp),
                    style = timeFontStyle,
                )
                Spacer(modifier = Modifier.width(4.dp))
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
                            max = if (chatMessage.direction == MessageDirection.RECEIVED) 218.dp else 253.dp
                        )
                    ,
                    text = messageContent.message,
                    style = caption,
                )
            }
            if (chatMessage.direction == MessageDirection.RECEIVED) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = DateTimeUtil.getFormattedTime(chatMessage.timestamp),
                    style = timeFontStyle,
                )
            }
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