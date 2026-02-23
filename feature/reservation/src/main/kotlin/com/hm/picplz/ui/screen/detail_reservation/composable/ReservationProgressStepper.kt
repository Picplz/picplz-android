@file:Suppress("UnusedPrivateMember")

package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStep
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStep.Companion.hasPassed
import com.hm.picplz.ui.theme.MainFontFamily.bodyBold
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ReservationProgressStepper(
    currentReservationStep: ReservationStep,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally),
    ) {
        ReservationStep.entries.forEachIndexed { index, step ->
            ReservationStepItem(
                isActive = step.hasPassed(currentReservationStep),
                label = stringResource(step.nameResId),
            )

            if (index < ReservationStep.entries.lastIndex) {
                DashLine()
            }
        }
    }
}

@Composable
private fun ReservationStepItem(
    isActive: Boolean,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StepIcon(isActive)

        StepLabel(label, isActive)
    }
}

@Composable
private fun StepIcon(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    val drawableResId = if (isActive) R.drawable.reservation_status_on else R.drawable.reservation_status_off
    val painter = painterResource(id = drawableResId)

    Image(
        painter = painter,
        contentDescription = description,
        modifier = modifier.size(52.dp),
    )
}

@Composable
private fun StepLabel(
    label: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        modifier = modifier,
        style = if (isActive) bodyBold else pretendardTypography.bodyMedium.copy(color = MainThemeColor.Gray4),
    )
}

@Composable
private fun DashLine(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.dash_line),
        contentDescription = "DashLine",
        modifier = modifier.width(42.dp).height(52.dp),
    )
}

@Preview
@Composable
private fun ReservationProgressStepperWaitingPreview() {
    ReservationProgressStepper(
        currentReservationStep = ReservationStep.WAITING,
    )
}

@Preview
@Composable
private fun ReservationProgressStepperWaitingInProgress() {
    ReservationProgressStepper(
        currentReservationStep = ReservationStep.IN_PROGRESS,
    )
}

@Preview
@Composable
private fun ReservationProgressStepperWaitingConfirmed() {
    ReservationProgressStepper(
        currentReservationStep = ReservationStep.CONFIRMED,
    )
}
