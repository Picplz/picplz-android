package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.KakaoMapView
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun DetailReservationMap(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        KakaoMapView(
            modifier = Modifier.fillMaxSize(),
        )

        CloseButton(
            modifier = Modifier.padding(start = 16.dp, top = 6.dp),
            onClick = onCloseClick,
        )
    }
}

@Composable
private fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier =
            modifier
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(32.dp),
                ).size(32.dp),
        colors =
            IconButtonDefaults
                .filledIconButtonColors(
                    containerColor = MainThemeColor.White,
                ),
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(R.drawable.close),
            contentDescription = "닫기",
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview
@Composable
private fun DetailReservationMapPreview() {
    DetailReservationMap(
        onCloseClick = {},
        modifier =
            Modifier
                .fillMaxWidth()
                .height(230.dp),
    )
}
