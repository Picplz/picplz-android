package com.hm.picplz.ui.screen.photographer_chat_room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.ui.screen.chat_room.dummyReservationChatMessages
import com.hm.picplz.ui.screen.composable.ChatRoomScreenContent
import com.hm.picplz.ui.screen.photographer_chat_room.composable.ReservationInfoBanner
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PhotographerChatRoomScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotographerChatRoomViewModel = hiltViewModel(),
    @Suppress("UNUSED_PARAMETER") _roomId: String,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is PhotographerChatRoomSideEffect.NavigateToPrev -> onNavigateBack()
            }
        }
    }

    ChatRoomScreenContent(
        modifier = modifier,
        title = "유가영",
        subtitle = "",
        reservationStep = state.reservationStep,
        chatMessages = state.chatMessages,
        onBackClick = {
            viewModel.handleIntent(PhotographerChatRoomIntent.NavigateToPrev)
        },
        onMenuClick = {
            // TODO: Implement menu click action
        },
        reservationInfoSection = {
            ReservationInfoBanner(
                customerName = state.customerName,
                productName = state.productName,
                customerProfileImageUri = state.customerImageUrl,
                onClick = {
                    // TODO: 예약 정보 화면으로 이동
                },
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PhotographerChatRoomScreenPreview() {
    PicplzTheme {
        ChatRoomScreenContent(
            title = "유가영",
            subtitle = "",
            reservationStep = PhotographerChatRoomState.idle().reservationStep,
            chatMessages = dummyReservationChatMessages,
            onBackClick = {},
            onMenuClick = {},
            reservationInfoSection = {
                ReservationInfoBanner(
                    customerName = "애니프사",
                    productName = "자연스러운프사",
                    customerProfileImageUri = null,
                    onClick = {},
                )
            },
        )
    }
}
