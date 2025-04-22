package com.hm.picplz.ui.screen.chat.composable

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
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatMessageBubble(
    modifier: Modifier = Modifier,
    content: String = "",
    direction: MessageDirection,
    user: User,
){
    Surface(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(20.dp),
        color = when(direction) {
            MessageDirection.SENT -> MainThemeColor.Gray1
            MessageDirection.RECEIVED -> MainThemeColor.White
        },
        border = when(direction) {
            MessageDirection.SENT -> null
            MessageDirection.RECEIVED ->
                BorderStroke(
                    width = 1.dp,
                    color = MainThemeColor.Gray3
                )
        }
    ) {
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
            text = content,
            style = caption,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageSentBubblePreview() {
    PicplzTheme {
        ChatMessageBubble(
            content = "말풍선 어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다",
            direction = MessageDirection.SENT,
            user = User(
                id = "1",
                nickname = "나",
                profileImageUri = null,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageReceivedBubblePreview() {
    PicplzTheme {
        ChatMessageBubble(
            content = "말풍선 어쩌고 저쩌고 가로 최대 크기는 이만큼 입니다",
            direction = MessageDirection.RECEIVED,
            user = User(
                id = "2",
                nickname = "유가영 작가",
                profileImageUri = null,
            )
        )
    }
}