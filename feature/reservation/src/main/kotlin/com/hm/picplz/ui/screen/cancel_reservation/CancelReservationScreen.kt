package com.hm.picplz.ui.screen.cancel_reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun CancelReservationScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CancelReservationScreenContent(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
private fun CancelReservationScreenContent(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text = "예약 정보 확인",
                onClickBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier,
        ) {
            // TODO: 메인 콘텐츠 추가 예정
        }
    }
}

@Preview
@Composable
private fun CancelReservationScreenPreview() {
    CancelReservationScreen()
}
