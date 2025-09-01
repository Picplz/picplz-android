package com.hm.picplz.ui.screen.chat_room.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.screen.chat_room.ReservationStep
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun ReservationStep(
    modifier: Modifier = Modifier,
    reservationStep: ReservationStep,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = when(reservationStep) {
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
                    reservationStep == ReservationStep.PENDING
                ) MainThemeColor.Gray3 else MainThemeColor.Black
            )
            Text(
                text = "거래 확정",
                style = caption,
                color = if (
                    reservationStep == ReservationStep.CONFIRMED
                ) MainThemeColor.Black else MainThemeColor.Gray3
            )
        }
    }
}