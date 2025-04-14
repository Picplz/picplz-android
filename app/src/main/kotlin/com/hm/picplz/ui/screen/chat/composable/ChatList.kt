package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.model.ChatRoomInfo
import com.hm.picplz.ui.model.ChatStatus
import com.hm.picplz.ui.model.Message
import com.hm.picplz.ui.screen.common.BadgeTheme
import com.hm.picplz.ui.screen.common.CommonBadge
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.utils.DateTimeUtil

@Composable
fun ChatList (
    modifier: Modifier = Modifier,
    chatRoomInfo: ChatRoomInfo
) {
    val timeText = DateTimeUtil.getTimeAgoText(chatRoomInfo.lastMessage.sentAt)

    Column(
        modifier = modifier
            .height(105.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CommonBadge(
                    label = when (chatRoomInfo.chatStatus) {
                        ChatStatus.PENDING -> "예약 대기"
                        ChatStatus.CONFIRMED -> "예약 확정"
                        ChatStatus.REJECTED -> "촬영 거절"
                        ChatStatus.COMPLETED -> "촬영 완료"
                    },
                    theme = when (chatRoomInfo.chatStatus) {
                        ChatStatus.PENDING -> BadgeTheme.INACTIVE
                        ChatStatus.CONFIRMED -> BadgeTheme.ACTIVE
                        ChatStatus.REJECTED -> BadgeTheme.INACTIVE
                        ChatStatus.COMPLETED -> BadgeTheme.ACTIVE
                    }
                )

                CommonBadge(
                    label = chatRoomInfo.packageType,
                    theme = BadgeTheme.DISABLED
                )
            }
            Text(
                text = timeText,
                style = caption,
                color = MainThemeColor.Gray3,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    PicplzTheme {
        ChatList(
            chatRoomInfo = ChatRoomInfo(
                chatStatus = ChatStatus.PENDING,
                packageType = "인스타 종합 패키지",
                lastMessage = Message(
                    profileImageUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=John",
                    nickname = "김도현",
                    message = "안녕하세요"
                )
            )
        )
    }
}