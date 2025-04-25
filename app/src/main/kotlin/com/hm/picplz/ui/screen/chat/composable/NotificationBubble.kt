package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ButtonActionType
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.MessageButton
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.model.NotificationType
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun NotificationBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = when(chatMessage.direction) {
            MessageDirection.SENT -> MainThemeColor.Gray1
            MessageDirection.RECEIVED -> MainThemeColor.White
        },
    ) {
        // 내부
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationBubblePreview() {
    PicplzTheme {
        NotificationBubble(
            chatMessage = ChatMessage(
                id = 1,
                direction = MessageDirection.RECEIVED,
                content = MessageContent.Notification(
                    title = "상품명",
                    subtitle = "긍정적인 안내",
                    content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
                    type = NotificationType.POSITIVE,
                    button = MessageButton(
                        text = "확인",
                        actionType = ButtonActionType.CONFIRM,
                    ),
                ),
                sender = User(
                    id = "1",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 100000,
            )
        )
    }
}