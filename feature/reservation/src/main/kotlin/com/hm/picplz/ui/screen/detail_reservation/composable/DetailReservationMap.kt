package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.annotation.DrawableRes
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
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle

@Composable
fun DetailReservationMap(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialPosition: LatLng = LatLng.from(37.406960, 127.115587),
) {
    Box(modifier = modifier) {
        KakaoMapView(
            modifier = Modifier.fillMaxSize(),
            initialPosition = initialPosition,
            onMapReady = { kakaoMap ->
                kakaoMap.labelManager?.layer?.addLabel(
                    LabelOptions.from(MARKER_ID, initialPosition).setStyles(
                        markerStyle(R.drawable.center_char_circle, ZOOM_LEVEL_DEFAULT),
                        markerStyle(R.drawable.center_char_circle_1_5, ZOOM_LEVEL_DETAILED),
                    ),
                )
            },
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

private fun markerStyle(
    @DrawableRes resId: Int,
    zoomLevel: Int,
): LabelStyle =
    LabelStyle
        .from(resId)
        .setZoomLevel(zoomLevel)
        .setAnchorPoint(ANCHOR_CENTER, ANCHOR_CENTER)

private const val ZOOM_LEVEL_DEFAULT = 0
private const val ZOOM_LEVEL_DETAILED = 10
private const val ANCHOR_CENTER = 0.5f
private const val MARKER_ID = "reservation_marker"

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
