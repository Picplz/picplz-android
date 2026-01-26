package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ReservationStatusHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        ReservationStatusInfo(
            title = title,
            description = description,
        )
    }
}

@Composable
private fun ReservationStatusInfo(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = pretendardTypography.titleMedium,
            color = MainThemeColor.Black,
        )
        Text(
            text = description,
            style = caption,
            color = MainThemeColor.Green120,
        )
    }
}

@Preview
@Composable
private fun ReservationStatusHeaderPreview() {
    ReservationStatusHeader(
        title = "예약 승인 대기 중...",
        description = "n분 이내로 승인되지 않으면 자동 취소됩니다.",
    )
}
