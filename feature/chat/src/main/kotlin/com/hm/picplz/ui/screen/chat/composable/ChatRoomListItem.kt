package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.domain.model.ChatRoomInfo
import com.hm.picplz.domain.model.ChatStatus
import com.hm.picplz.domain.model.Message
import com.hm.picplz.ui.screen.common.BadgeTheme
import com.hm.picplz.ui.screen.common.CommonBadge
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.common.util.DateTimeUtil

@Composable
fun ChatRoomListItem (
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    chatRoomInfo: ChatRoomInfo
) {
    val timeText = DateTimeUtil.getTimeAgoText(chatRoomInfo.lastMessage.sentAt)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
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
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row (
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = chatRoomInfo.lastMessage.profileImageUrl),
                    contentDescription = "최신 메세지 프로필",
                    modifier = Modifier
                        .size(37.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MainThemeColor.Black,
                            shape = CircleShape
                        )
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 45.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = chatRoomInfo.lastMessage.nickname,
                        style = pretendardTypography.bodyMedium,
                        color = MainThemeColor.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = chatRoomInfo.lastMessage.message,
                        style = caption,
                        color = MainThemeColor.Gray5,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (chatRoomInfo.unreadMessageCount > 0) {
                Box(
                    modifier = modifier
                        .height(37.dp),
                ) {
                    val displayCount = if (chatRoomInfo.unreadMessageCount > 999)
                        "999+"
                    else
                        chatRoomInfo.unreadMessageCount.toString()

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MainThemeColor.Red)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayCount,
                            color = MainThemeColor.White,
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            } else {
                 Spacer(modifier = Modifier.width(18.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatRoomListItemPreview() {
    PicplzTheme {
        ChatRoomListItem(
            chatRoomInfo = ChatRoomInfo(
                id = "1",
                chatStatus = ChatStatus.PENDING,
                packageType = "인스타 종합 패키지",
                lastMessage = Message(
                    profileImageUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=John",
                    nickname = "합정동 불주먹",
                    message = "촬영 예약이 도착했습니다. 60분 이내에 답변 안 할 시 예약이 취소될 수 있습니다.",
                ),
                unreadMessageCount = 1
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRoomListItemOverThousandPreview() {
    PicplzTheme {
        ChatRoomListItem(
            chatRoomInfo = ChatRoomInfo(
                id = "1",
                chatStatus = ChatStatus.PENDING,
                packageType = "프로필 Only.",
                lastMessage = Message(
                    profileImageUrl = "https://api.dicebear.com/7.x/avataaars/png?seed=John",
                    nickname = "합정동 불주먹",
                    message = "촬영 예약이 도착했습니다. 60분 이내에 답변 안 할 시 예약이 취소될 수 있습니다.",
                ),
                unreadMessageCount = 12312
            )
        )
    }
}