package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun RefundGuideContent(
    shootingDateFormatted: String,
    cancelDateFormatted: String,
    totalPrice: Int,
    refundPrice: Int,
    cancellationFee: Int,
    isAgreementChecked: Boolean,
    onAgreementChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(color = MainThemeColor.White),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            item {
                Text(
                    text = stringResource(R.string.refund_guide_title),
                    style = MainThemeFont.Title,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                )
            }

            item {
                RefundDateInfoSection(
                    shootingDateFormatted = shootingDateFormatted,
                    cancelDateFormatted = cancelDateFormatted,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 28.dp),
                )
            }

            item {
                RefundAmountSection(
                    totalPrice = totalPrice,
                    refundPrice = refundPrice,
                    cancellationFee = cancellationFee,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
                )
            }

            item {
                RefundPolicySection(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
                )
            }

            item {
                CheckboxWithLabel(
                    text = stringResource(R.string.refund_agreement_text),
                    isSelected = isAgreementChecked,
                    onToggle = { onAgreementChange(!isAgreementChecked) },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RefundGuideContentPreview() {
    PicplzTheme {
        RefundGuideContent(
            shootingDateFormatted = "25.01.09",
            cancelDateFormatted = "25.01.05",
            totalPrice = 12900,
            refundPrice = 11610,
            cancellationFee = 1290,
            isAgreementChecked = false,
            onAgreementChange = {},
        )
    }
}
