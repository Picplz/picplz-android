package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun RefundAmountSection(
    totalPrice: Int,
    refundPrice: Int,
    cancellationFee: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.refund_amount_title),
            style =
                MainThemeFont.BodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            color = MainThemeColor.Black,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        HorizontalDivider(
            color = MainThemeColor.Black,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        RefundAmountRow(
            label = {
                Text(
                    text = stringResource(R.string.refund_total_amount),
                    style = MainThemeFont.BodyBold,
                    color = MainThemeColor.Black,
                )
            },
            amount = refundPrice,
            isTotalAmount = true,
        )

        Spacer(modifier = Modifier.height(9.5.dp))

        RefundAmountRow(
            label = {
                Text(
                    text = stringResource(R.string.refund_paid_amount),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                )
            },
            amount = totalPrice,
            isTotalAmount = false,
        )

        Spacer(modifier = Modifier.height(4.dp))

        RefundAmountRow(
            label = {
                LabelWithHighlight(
                    labelResId = R.string.refund_cancellation_fee_label,
                    highlightResId = R.string.refund_cancellation_fee_rate,
                    highlightColor = Color(0xFFEF4747),
                )
            },
            amount = cancellationFee,
            isTotalAmount = false,
        )
    }
}

@Composable
private fun RefundAmountRow(
    label: @Composable () -> Unit,
    amount: Int,
    isTotalAmount: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        label()
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.amount_format, amount),
            style =
                if (isTotalAmount) {
                    MainThemeFont.BodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    )
                } else {
                    MainThemeFont.Body
                },
            color = if (isTotalAmount) Color(0xFFEF4747) else MainThemeColor.Gray4,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RefundAmountSectionPreview() {
    PicplzTheme {
        RefundAmountSection(
            totalPrice = 12900,
            refundPrice = 11610,
            cancellationFee = 1290,
        )
    }
}
