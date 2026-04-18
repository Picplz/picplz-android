package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.cancel_reservation.CancelReservationPagerPage
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CancelReservationStepIndicator(
    currentPage: CancelReservationPagerPage,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CancelReservationPagerPage.entries.forEach { page ->
            StepBadge(
                stepNumber = page.ordinal + 1,
                isActive = currentPage == page,
            )
        }
    }
}

@Composable
private fun StepBadge(
    stepNumber: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(22.dp)
                .background(
                    color = if (isActive) MainThemeColor.Green120 else MainThemeColor.Gray1,
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stepNumber.toString(),
            style = MainThemeFont.BodySmallButton2,
            color = if (isActive) MainThemeColor.White else MainThemeColor.Gray5,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelReservationStepIndicatorPreviewStep1() {
    PicplzTheme {
        CancelReservationStepIndicator(
            currentPage = CancelReservationPagerPage.REASON_INPUT,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelReservationStepIndicatorPreviewStep2() {
    PicplzTheme {
        CancelReservationStepIndicator(
            currentPage = CancelReservationPagerPage.REFUND_GUIDE,
        )
    }
}
