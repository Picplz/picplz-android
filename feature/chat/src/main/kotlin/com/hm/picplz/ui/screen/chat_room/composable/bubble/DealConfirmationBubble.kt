package com.hm.picplz.ui.screen.chat_room.composable.bubble

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.common.model.User
import com.hm.picplz.domain.model.ButtonActionType
import com.hm.picplz.domain.model.ChatMessage
import com.hm.picplz.domain.model.MessageButton
import com.hm.picplz.domain.model.MessageContent
import com.hm.picplz.domain.model.MessageDirection
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun DealConfirmationBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
    onButtonClick: ((MessageButton) -> Unit)? = null,
) {
    val messageContent = chatMessage.content as MessageContent.DealConfirmation

    ChatBubbleSurface(
        modifier =
            modifier
                .width(238.dp),
        direction = chatMessage.direction,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(18.dp),
        ) {
            Text(
                text =
                    if (chatMessage.direction == MessageDirection.SENT) {
                        "거래 확정을 요청했어요!"
                    } else {
                        "거래 확정 요청"
                    },
                style = MainFontFamily.bodyBold,
                color = MainThemeColor.Black,
            )
            Spacer(
                modifier = Modifier.height(10.dp),
            )
            Text(
                text =
                    if (chatMessage.direction == MessageDirection.SENT) {
                        "${chatMessage.receiver.nickname}님의 거래 확정을 기다리고 있어요"
                    } else {
                        "사진 촬영본을 잘 받으셨나요\n" +
                            "빠른 정산을 위해 거래를 확정해 주세요.\n" +
                            "7일 이후에, 자동으로 거래 확정 처리가\n" +
                            "진행됩니다."
                    },
                style = MainFontFamily.caption,
                color = MainThemeColor.Gray5,
            )
            if (chatMessage.direction == MessageDirection.RECEIVED) {
                Spacer(modifier = Modifier.height(10.dp))
                val button = messageContent.button
                if (button != null && onButtonClick != null) {
                    ChatBubbleButton(
                        onClick = { onButtonClick(button) },
                        text = button.text,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text =
                        "작가의 잠수 , 일방적 취소, 사진 미전달 등으로\n" +
                            "성공적인 촬영이 이루어지지 않았나요?",
                    style =
                        TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                            lineHeight = 10.sp * 1.4,
                            letterSpacing = 0.sp,
                            textDecoration = TextDecoration.Underline,
                        ),
                    color = MainThemeColor.Red,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DealConfirmationBubbleSentPreview() {
    PicplzTheme {
        DealConfirmationBubble(
            chatMessage =
                ChatMessage(
                    id = 1,
                    direction = MessageDirection.SENT,
                    content = MessageContent.DealConfirmation(),
                    timestamp = System.currentTimeMillis() - 100000,
                    sender =
                        User(
                            id = "2",
                            nickname = "나",
                            profileImageUri = null,
                        ),
                    receiver =
                        User(
                            id = "1",
                            nickname = "유가영 작가",
                            profileImageUri = null,
                        ),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DealConfirmationBubbleReceivedPreview() {
    PicplzTheme {
        DealConfirmationBubble(
            chatMessage =
                ChatMessage(
                    id = 1,
                    direction = MessageDirection.RECEIVED,
                    content =
                        MessageContent.DealConfirmation(
                            MessageButton(
                                text = "거래 확정",
                                actionType = ButtonActionType.CONFIRM_ORDER,
                            ),
                        ),
                    timestamp = System.currentTimeMillis() - 100000,
                    sender =
                        User(
                            id = "1",
                            nickname = "유가영 작가",
                            profileImageUri = null,
                        ),
                    receiver =
                        User(
                            id = "2",
                            nickname = "나",
                            profileImageUri = null,
                        ),
                ),
            onButtonClick = {},
        )
    }
}
