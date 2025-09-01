package com.hm.picplz.ui.screen.chat_room

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.model.ButtonActionType
import com.hm.picplz.ui.model.MessageContent
import com.hm.picplz.ui.model.MessageDirection
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ChatMessageBubble
import com.hm.picplz.ui.screen.chat_room.composable.ChatMessageProfile
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ChangeTimeBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ChatSuggest
import com.hm.picplz.ui.screen.chat_room.composable.bubble.CompleteBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.DealConfirmationBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ImageChat
import com.hm.picplz.ui.screen.chat_room.composable.bubble.NotificationBubble
import com.hm.picplz.ui.screen.chat_room.composable.ChatInput
import com.hm.picplz.ui.screen.chat_room.composable.ReservationStep
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
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            ReservationStep(
                reservationStep = currentState.reservationStep
            )
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
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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
                                    is MessageContent.ChangeTime -> Alignment.Top
                                    is MessageContent.DealConfirmation -> Alignment.Top
                                    is MessageContent.ChatSuggest -> Alignment.Top
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
                                    if (item.message.direction == MessageDirection.SENT
                                        && item.message.content !is MessageContent.ChatSuggest
                                    ) {
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
                                        is MessageContent.Image -> {
                                            ImageChat(
                                                imageUris = item.message.content.imageUris,
                                            )
                                        }
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
                                        is MessageContent.ChangeTime -> {
                                            ChangeTimeBubble(
                                                chatMessage = item.message
                                            )
                                        }
                                        is MessageContent.DealConfirmation -> {
                                            DealConfirmationBubble(
                                                chatMessage = item.message,
                                                onButtonClick = {}
                                            )
                                        }
                                        is MessageContent.ChatSuggest -> {
                                            ChatSuggest(
                                                suggestedChats = item.message.content.suggestedChats
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
            ChatInput()
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