package com.hm.picplz.ui.screen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.domain.model.ButtonActionType
import com.hm.picplz.domain.model.ChatMessage
import com.hm.picplz.domain.model.MessageContent
import com.hm.picplz.domain.model.MessageDirection
import com.hm.picplz.ui.screen.chat_room.ChatListItem
import com.hm.picplz.ui.screen.chat_room.ReservationStep
import com.hm.picplz.ui.screen.chat_room.composable.ChatInput
import com.hm.picplz.ui.screen.chat_room.composable.ChatMessageProfile
import com.hm.picplz.ui.screen.chat_room.composable.ReservationStep
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ChangeTimeBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ChatMessageBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ChatSuggest
import com.hm.picplz.ui.screen.chat_room.composable.bubble.CompleteBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.DealConfirmationBubble
import com.hm.picplz.ui.screen.chat_room.composable.bubble.ImageChat
import com.hm.picplz.ui.screen.chat_room.composable.bubble.NotificationBubble
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@Composable
internal fun ChatRoomScreenContent(
    title: String,
    subtitle: String,
    reservationStep: ReservationStep,
    chatMessages: List<ChatMessage>,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier,
    reservationInfoSection: @Composable (() -> Unit)? = null,
) {
    val chatListItem =
        remember(chatMessages) {
            chatMessages
                .groupBy { chat ->
                    DateTimeUtil.truncateToDate(chat.timestamp)
                }.flatMap { (date, rest) ->
                    buildList {
                        add(ChatListItem.DateHeader(date))
                        addAll(rest.map { ChatListItem.MessageItem(it) })
                    }
                }
        }

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            ChatRoomTopBar(
                title = title,
                subtitle = subtitle,
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
            )
        },
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .padding(innerPadding),
        ) {
            ReservationStep(
                reservationStep = reservationStep,
            )

            HorizontalDivider(
                color = MainThemeColor.Gray2,
            )

            reservationInfoSection?.invoke()
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(
                    items = chatListItem,
                    key = { item ->
                        when (item) {
                            is ChatListItem.DateHeader -> "date_${item.date}"
                            is ChatListItem.MessageItem -> "message_${item.message.id}"
                        }
                    },
                ) { item ->
                    when (item) {
                        is ChatListItem.DateHeader -> {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = DateTimeUtil.getFormattedDate(item.date),
                                    style = caption,
                                    color = MainThemeColor.Black,
                                )
                            }
                        }

                        is ChatListItem.MessageItem -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment =
                                    when (item.message.content) {
                                        is MessageContent.Text -> Alignment.CenterVertically
                                        is MessageContent.Image -> Alignment.Top
                                        is MessageContent.Notification -> Alignment.Top
                                        is MessageContent.Completion -> Alignment.Top
                                        is MessageContent.ChangeTime -> Alignment.Top
                                        is MessageContent.DealConfirmation -> Alignment.Top
                                        is MessageContent.ChatSuggest -> Alignment.Top
                                    },
                                horizontalArrangement =
                                    if (item.message.direction === MessageDirection.RECEIVED) {
                                        Arrangement.Start
                                    } else {
                                        Arrangement.End
                                    },
                            ) {
                                if (item.message.direction == MessageDirection.RECEIVED) {
                                    ChatMessageProfile(
                                        profileImageUri = item.message.sender.profileImageUri,
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    val timeFontStyle =
                                        TextStyle(
                                            fontFamily = Pretendard,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 8.sp,
                                            lineHeight = 8.sp * 1.4,
                                            letterSpacing = 0.sp,
                                            color = MainThemeColor.Gray3,
                                        )
                                    if (item.message.direction == MessageDirection.SENT &&
                                        item.message.content !is MessageContent.ChatSuggest
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
                                            val imageContent = item.message.content as MessageContent.Image
                                            ImageChat(
                                                imageUris = imageContent.imageUris,
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
                                                },
                                            )
                                        }

                                        is MessageContent.Completion -> {
                                            CompleteBubble(
                                                chatMessage = item.message,
                                            )
                                        }

                                        is MessageContent.ChangeTime -> {
                                            ChangeTimeBubble(
                                                chatMessage = item.message,
                                            )
                                        }

                                        is MessageContent.DealConfirmation -> {
                                            DealConfirmationBubble(
                                                chatMessage = item.message,
                                                onButtonClick = {},
                                            )
                                        }

                                        is MessageContent.ChatSuggest -> {
                                            val suggestContent = item.message.content as MessageContent.ChatSuggest
                                            ChatSuggest(
                                                suggestedChats = suggestContent.suggestedChats,
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
