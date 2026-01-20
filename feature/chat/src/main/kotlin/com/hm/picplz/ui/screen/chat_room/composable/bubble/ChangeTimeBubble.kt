package com.hm.picplz.ui.screen.chat_room.composable.bubble

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.common.model.User
import com.hm.picplz.common.util.DateTimeUtil.getFormattedDateTime
import com.hm.picplz.domain.model.ChatMessage
import com.hm.picplz.domain.model.MessageContent
import com.hm.picplz.domain.model.MessageDirection
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChangeTimeBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
) {
    val messageContent = chatMessage.content as MessageContent.ChangeTime
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
                text = "약속 시간을 변경했습니다.",
                style = MainFontFamily.bodyBold,
            )
            Spacer(
                modifier = Modifier.height(10.dp),
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style =
                            MainFontFamily.caption.toSpanStyle().copy(
                                color = MainThemeColor.Green120,
                            ),
                    ) {
                        append(getFormattedDateTime(messageContent.newScheduledTime))
                    }
                    withStyle(
                        style =
                            MainFontFamily.caption.toSpanStyle().copy(
                                color = MainThemeColor.Gray5,
                            ),
                    ) {
                        append("로\n" + "촬영 시간이 변경되었어요")
                    }
                },
                style =
                    TextStyle(
                        lineHeight = 12.sp * 1.4,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangeTimeBubblePreview() {
    PicplzTheme {
        ChangeTimeBubble(
            chatMessage =
                ChatMessage(
                    id = 1,
                    direction = MessageDirection.SENT,
                    content =
                        MessageContent.ChangeTime(
                            newScheduledTime = System.currentTimeMillis() - 100000,
                        ),
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
                    timestamp = System.currentTimeMillis() - 100000,
                ),
        )
    }
}
