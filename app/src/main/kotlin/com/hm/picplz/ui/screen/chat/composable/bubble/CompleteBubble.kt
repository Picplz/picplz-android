package com.hm.picplz.ui.screen.chat.composable.bubble

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.data.model.User
import com.hm.picplz.ui.model.ChatMessage
import com.hm.picplz.ui.model.DeliveryType
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.utils.DateTimeUtil.getFormattedDeadline

@Composable
fun CompleteBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage
) {
    val messageContent = chatMessage.content as MessageContent.Completion
    ChatBubbleSurface(
        modifier = modifier
            .width(238.dp),
        direction = chatMessage.direction,
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
        ){
            Text(
                text = messageContent.title,
                style = pretendardTypography.titleSmall
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                text = "촬영을 완료했어요!",
                color = MainThemeColor.Green120,
                style = MainFontFamily.bodyBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = MainFontFamily.insideTag.toSpanStyle().copy()
                    ) {
                        append("전달 방식: ")
                    }
                    withStyle(style = MainFontFamily.caption.toSpanStyle()
                    ) {
                        append(
                            when(messageContent.deliveryMethod) {
                                DeliveryType.EMAIL -> "이메일"
                            }
                        )
                    }
                },
                style = TextStyle(letterSpacing = 12.sp * 1.4),
                color = MainThemeColor.Gray5,
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = MainFontFamily.insideTag.toSpanStyle(),
                    ) {
                        append("전달 기한: ")
                    }
                    withStyle(
                        style = MainFontFamily.caption.toSpanStyle()
                    ) {
                        append(getFormattedDeadline(messageContent.deliveryDeadline))
                    }
                },
                color = MainThemeColor.Gray5,
            )
            if (chatMessage.direction == MessageDirection.RECEIVED) {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(
                    color = MainThemeColor.Gray2,
                    thickness = 1.dp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "즐거운 촬영 되셨나요? 추가 요청사항이 있다면 작가에게 문의해주세요.",
                    style = MainFontFamily.caption,
                    color = MainThemeColor.Gray5,
                )
            }
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
                direction = MessageDirection.SENT,
                sender = User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
                receiver = User(
                    id = "2",
                    nickname = "유가영 작가",
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
                receiver = User(
                    id = "1",
                    nickname = "나",
                    profileImageUri = null,
                ),
                timestamp = System.currentTimeMillis() - 1000,
            )
        )
    }
}