package com.hm.picplz.ui.screen.chat_room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.ui.screen.composable.ChatRoomScreenContent
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatRoomScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel = hiltViewModel(),
    @Suppress("UNUSED_PARAMETER") _roomId: String,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ChatRoomSideEffect.NavigateToPrev -> onNavigateBack()
            }
        }
    }

    ChatRoomScreenContent(
        modifier = modifier,
        title = "유가영 작가",
        subtitle = "당장 촬영 가능",
        reservationStep = state.reservationStep,
        chatMessages = dummyChatMessages,
        onBackClick = {
            viewModel.handleIntent(ChatRoomIntent.NavigateToPrev)
        },
        onMessageButtonClick = {},
        onMenuClick = {
            // TODO: Implement menu click action
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomScreenPreview() {
    PicplzTheme {
        ChatRoomScreenContent(
            title = "유가영 작가",
            subtitle = "당장 촬영 가능",
            reservationStep = ChatRoomState.idle().reservationStep,
            chatMessages = dummyChatMessages,
            onBackClick = {},
            onMenuClick = {},
            onMessageButtonClick = {},
        )
    }
}
