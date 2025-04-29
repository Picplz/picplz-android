package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ButtonActionType
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.MessageButton
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.model.NotificationType
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun NotificationBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
    onButtonClick: ((MessageButton) -> Unit)? = null
) {
    val messageContent = chatMessage.content as MessageContent.Notification
    ChatBubbleSurface(
        modifier = modifier
            .width(238.dp),
        direction = chatMessage.direction,
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
        ) {
            if (messageContent.title !== null) {
                Text(
                    text = messageContent.title,
                    style = pretendardTypography.titleSmall
                )
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
            }
            if (messageContent.subtitle !== null) {
                Text(
                    text = messageContent.subtitle,
                    color =
                        if (chatMessage.direction == MessageDirection.RECEIVED) {
                            when(messageContent.type) {
                                NotificationType.POSITIVE -> MainThemeColor.Green120
                                NotificationType.NEGATIVE -> MainThemeColor.Red
                            }
                        } else {
                            MainThemeColor.Black
                        },
                    style = MainFontFamily.bodyBold
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
            Text(
                text = messageContent.content,
                style = MainFontFamily.caption,
                color =
                    if(chatMessage.direction == MessageDirection.SENT && messageContent.title == null)
                        Color(0xff5A6A76)
                    else
                        MainThemeColor.Gray5
            )
            if (messageContent.caption !== null) {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                Text(
                    text = messageContent.caption,
                    style = TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                    ),
                    color = MainThemeColor.Gray4
                )
            }
            if(messageContent.button !== null && onButtonClick != null) {
                Spacer(
                    modifier = Modifier
                        .then(
                            if (messageContent.caption !== null) {
                                Modifier.height(6.dp)
                            } else {
                                Modifier.height(10.dp)
                            }
                        )
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainThemeColor.Black,
                        contentColor = MainThemeColor.White
                    ),
                    onClick = { onButtonClick(messageContent.button) }
                ) {
                    Text(
                        text = messageContent.button.text,
                        style = MainFontFamily.buttonChat
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationBubblePreview() {
    PicplzTheme {
        NotificationBubble(
            chatMessage = ChatMessage(
                id = 1,
                direction = MessageDirection.SENT,
                content = MessageContent.Notification(
                    title = "상품명",
                    subtitle = "긍정적인 안내",
                    content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
                    type = NotificationType.POSITIVE,
                    button = MessageButton(
                        text = "확인",
                        actionType = ButtonActionType.FIND_ANOTHER_ARTIST
                    ),
                ),
                sender = User(
                    id = "1",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 100000,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationBubbleButtonPreview() {
    PicplzTheme {
        NotificationBubble(
            chatMessage = ChatMessage(
                id = 1,
                direction = MessageDirection.RECEIVED,
                content = MessageContent.Notification(
                    title = "상품명",
                    subtitle = "부정적인 안내",
                    content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
                    type = NotificationType.NEGATIVE,
                    button = MessageButton(
                        text = "확인",
                        actionType = ButtonActionType.FIND_ANOTHER_ARTIST
                    ),
                ),
                sender = User(
                    id = "1",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 100000,
            ),
            onButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationBubbleSendPreview() {
    PicplzTheme {
        NotificationBubble(
            chatMessage = ChatMessage(
                id = 1,
                direction = MessageDirection.SENT,
                content = MessageContent.Notification(
                    title = "상품명",
                    subtitle = "긍정적인 안내",
                    content = "대통령은 헌법과 법률이 정하는 바에\n의하여 공무원을 임면한다.",
                    type = NotificationType.POSITIVE,
                    button = MessageButton(
                        text = "확인",
                        actionType = ButtonActionType.FIND_ANOTHER_ARTIST
                    ),
                    caption = "캡션"
                ),
                sender = User(
                    id = "1",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 100000,
            ),
            onButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationBubbleCaptionButtonPreview() {
    PicplzTheme {
        NotificationBubble(
            chatMessage = ChatMessage(
                id = 1,
                direction = MessageDirection.SENT,
                content = MessageContent.Notification(
                    subtitle = "주문서를 전송했어요",
                    content = "작가가 내용을 확인 중입니다. \n" +
                            "최종 승인 후 촬영이 진행됩니다.",
                    type = NotificationType.POSITIVE,
                    button = MessageButton(
                        text = "주문서 확인하기",
                        actionType = ButtonActionType.FIND_ANOTHER_ARTIST
                    ),
                ),
                sender = User(
                    id = "1",
                    nickname = "유가영 작가",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 100000,
            ),
            onButtonClick = {}
        )
    }
}