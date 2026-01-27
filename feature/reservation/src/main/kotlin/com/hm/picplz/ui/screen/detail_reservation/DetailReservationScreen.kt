package com.hm.picplz.ui.screen.detail_reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationInfoSection
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationProgressStepper
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationStatusHeader
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun DetailReservationScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(modifier = Modifier.fillMaxWidth().height(230.dp).background(Color.LightGray)) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "지도 영역",
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                item {
                    ReservationStatusHeader(
                        modifier = Modifier.padding(vertical = 20.dp),
                        title = "예약 승인 대기중...",
                        description = "n분 이내로 승인되지 않으면 자동 취소됩니다.",
                        onCancelClick = {},
                    )
                }

                item {
                    ReservationProgressStepper(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    )
                }

                item {
                    ReservationInfoSection(modifier = Modifier.padding(top = 28.dp, bottom = 24.dp))
                }
            }

            CommonBottomButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                text = "채팅 바로가기",
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
fun DetailReservationScreenPreview() {
    DetailReservationScreen()
}
