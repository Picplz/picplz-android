package com.hm.picplz.ui.screen.cancel_reservation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.core.ui.R as CoreR

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
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
        ) {
            CancelReservationCheckIcon()

            CancelReservationTitle()

            CancelReservationDescription()

            Spacer(modifier = Modifier.weight(1f))

            CancelReservationGuide()
        }
    }
}

@Composable
private fun CancelReservationCheckIcon(modifier: Modifier = Modifier) {
    Image(
        modifier =
            modifier
                .padding(top = 40.dp, bottom = 24.dp)
                .fillMaxWidth(),
        painter = painterResource(CoreR.drawable.check_circle),
        contentDescription = "예약 취소 완료",
    )
}

@Composable
private fun CancelReservationTitle(modifier: Modifier = Modifier) {
    Text(
        modifier =
            modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
        text = stringResource(R.string.cancel_reservation_title),
        style = pretendardTypography.titleLarge,
        color = MainThemeColor.Black,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun CancelReservationDescription(modifier: Modifier = Modifier) {
    Text(
        modifier =
            modifier
                .fillMaxWidth(),
        text = stringResource(R.string.cancel_reservation_description),
        style = pretendardTypography.bodyLarge,
        color = MainThemeColor.Black,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun CancelReservationGuide(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.cancel_reservation_guide),
            style = pretendardTypography.bodyMedium,
            color = MainThemeColor.Gray4,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.cancel_reservation_notice_prefix),
            style = pretendardTypography.bodySmall,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.cancel_reservation_notice_suffix),
            style = pretendardTypography.bodySmall,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun CancelReservationScreenPreview() {
    CancelReservationScreen()
}
