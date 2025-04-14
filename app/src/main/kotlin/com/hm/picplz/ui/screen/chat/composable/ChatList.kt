package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.R
import com.hm.picplz.ui.model.ChatRoomInfo
import com.hm.picplz.ui.screen.chat.ChatTabType
import com.hm.picplz.ui.screen.chat.dummyChatRooms
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ChatList (
    modifier: Modifier = Modifier,
    chatRooms: List<ChatRoomInfo>,
    chatTabType: ChatTabType = ChatTabType.ONGOING
) {
    if (chatRooms.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column (
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .offset(y = (-60).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val chatStatusText = when (chatTabType) {
                    ChatTabType.ONGOING -> "진행중인 채팅이 없습니다"
                    ChatTabType.COMPLETED -> "완료된 채팅이 없습니다"
                }

                Text(
                    text = chatStatusText,
                    style = pretendardTypography.headlineMedium
                )
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                )
                Text(
                    text = "작가님과 촬영을 시작해보세요",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = (16 * 1.4).sp,
                        letterSpacing = 0.sp,
                    ),
                    color = Color(0xFF5A6A76)
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Button(
                    onClick = {},
                    modifier = Modifier
                        .width(150.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainThemeColor.Black,
                        contentColor = MainThemeColor.White
                    )
                ) {
                    Text(
                        text = "둘러보기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.empty_character),
                contentDescription = "캐릭터 이미지",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 28.dp)
                    .offset(x = 25.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(chatRooms) { chatRoom ->
                ChatListItem(chatRoomInfo = chatRoom)
                if (chatRoom !== dummyChatRooms.last()) {
                    HorizontalDivider(
                        thickness = 6.dp,
                        color = MainThemeColor.Gray1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    PicplzTheme {
        ChatList(chatRooms = dummyChatRooms)
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyChatListPreview() {
    PicplzTheme {
        ChatList(chatRooms = emptyList())
    }
}