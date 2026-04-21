package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun RefundDateInfoSection(
    shootingDateFormatted: String,
    cancelDateFormatted: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(5.dp),
                    color = MainThemeColor.Gray2,
                ).padding(
                    horizontal = 14.dp,
                    vertical = 10.dp,
                ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.refund_shooting_date),
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = shootingDateFormatted,
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.refund_cancel_date),
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = cancelDateFormatted,
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RefundDateInfoSectionPreview() {
    PicplzTheme {
        RefundDateInfoSection(
            shootingDateFormatted = "25.01.09",
            cancelDateFormatted = "25.01.05",
        )
    }
}
