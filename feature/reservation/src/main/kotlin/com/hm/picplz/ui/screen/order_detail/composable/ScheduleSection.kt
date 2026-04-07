package com.hm.picplz.ui.screen.order_detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor.Black
import com.hm.picplz.ui.theme.MainThemeColor.Gray5
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun ScheduleSection(
    shootingTime: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.order_detail_section_shooting_schedule),
            style = MainThemeFont.ButtonDefault,
            color = Black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = shootingTime,
            style = MainThemeFont.Body,
            color = Gray5,
        )
    }
}

@Preview
@Composable
private fun ScheduleSectionPreview() {
    ScheduleSection(
        shootingTime = "25.01.09 - 오후 02:00",
    )
}
