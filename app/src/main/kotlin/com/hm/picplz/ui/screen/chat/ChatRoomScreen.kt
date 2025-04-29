package com.hm.picplz.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.model.ButtonActionType
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.screen.chat.composable.bubble.ChatMessageBubble
import com.hm.picplz.ui.screen.chat.composable.ChatMessageProfile
import com.hm.picplz.ui.screen.chat.composable.bubble.CompleteBubble
import com.hm.picplz.ui.screen.chat.composable.bubble.NotificationBubble
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.utils.DateTimeUtil
import com.hm.picplz.viewmodel.ChatRoomViewModel

@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel = hiltViewModel(),
    navController: NavHostController,
    roomId: Int,
) {
    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text = "유가영 작가",
                subText = "당장 촬영 가능",
                subTextStyle = caption.copy(color = MainThemeColor.Green120),
                onClickBack = {},
                showMenuIcon = true,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = when(currentState.reservationStep) {
                            ReservationStep.NOT_STARTED -> R.drawable.reservation_step_zero
                            ReservationStep.PENDING -> R.drawable.reservation_step_one
                            ReservationStep.IN_PROGRESS -> R.drawable.reservation_step_two
                            ReservationStep.CONFIRMED -> R.drawable.reservation_step_three
                        }
                    ),
                    contentDescription = "레벨"
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "예약 대기",
                        style = caption,
                        color = MainThemeColor.Black
                    )
                    Text(
                        text = "서비스 진행",
                        style = caption,
                        color = if (
                            currentState.reservationStep == ReservationStep.PENDING
                        ) MainThemeColor.Gray3 else MainThemeColor.Black
                    )
                    Text(
                        text = "거래 확정",
                        style = caption,
                        color = if (
                            currentState.reservationStep == ReservationStep.CONFIRMED
                        ) MainThemeColor.Black else MainThemeColor.Gray3
                    )
                }
            }
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                val chatListItem = dummyChatMessages
                    .groupBy { chat ->
                        DateTimeUtil.truncateToDate(chat.timestamp)
                    }
                    .flatMap { (date, rest) ->
                        buildList {
                            add(ChatListItem.DateHeader(date))
                            addAll(rest.map { ChatListItem.MessageItem(it) })
                        }
                    }

                items(
                    items = chatListItem,
                    key = { item ->
                        when (item) {
                            is ChatListItem.DateHeader -> "date_${item.date}"
                            is ChatListItem.MessageItem -> "message_${item.message.id}"
                        }
                    }
                ) { item ->
                    when (item) {
                        is ChatListItem.DateHeader -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp, bottom = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = DateTimeUtil.getFormattedDate(item.date),
                                    style = caption,
                                    color = MainThemeColor.Black
                                )
                            }
                        }
                        is ChatListItem.MessageItem -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = when (item.message.content) {
                                    is MessageContent.Text -> Alignment.CenterVertically
                                    is MessageContent.Image -> Alignment.Top
                                    is MessageContent.Notification -> Alignment.Top
                                    is MessageContent.Completion -> Alignment.Top
                                },
                                horizontalArrangement = if (item.message.direction === MessageDirection.RECEIVED) Arrangement.Start else Arrangement.End
                            ) {
                                if (item.message.direction == MessageDirection.RECEIVED) {
                                    ChatMessageProfile(
                                        profileImageUri = item.message.sender.profileImageUri
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
                                    if (item.message.direction == MessageDirection.SENT) {
                                        Text(
                                            text = DateTimeUtil.getFormattedTime(item.message.timestamp),
                                            style = timeFontStyle,
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    when (item.message.content) {
                                        is MessageContent.Text -> {
                                            ChatMessageBubble(
                                                chatMessage = item.message,
                                            )
                                        }
                                        is MessageContent.Image -> {}
                                        is MessageContent.Notification -> {
                                            NotificationBubble(
                                                chatMessage = item.message,
                                                onButtonClick = { button ->
                                                    when (button.actionType) {
                                                        ButtonActionType.OPEN_ORDER_FORM -> {}
                                                        ButtonActionType.FIND_ANOTHER_ARTIST -> {}
                                                        ButtonActionType.CONFIRM_ORDER -> {}
                                                        ButtonActionType.OPEN_URL -> {}
                                                    }
                                                }
                                            )
                                        }
                                        is MessageContent.Completion -> {
                                            CompleteBubble(
                                                chatMessage = item.message,
                                            )
                                        }
                                    }
                                    if (item.message.direction == MessageDirection.RECEIVED) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = DateTimeUtil.getFormattedTime(item.message.timestamp),
                                            style = timeFontStyle,
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRoomScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        ChatRoomScreen(
            navController = navController,
            roomId = 1
        )
    }
}