package com.hm.picplz.ui.screen.cancel_reservation.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CancelReservationTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonTopBar(
        modifier = modifier,
        text = stringResource(R.string.cancel_reservation_top_bar_title),
        onClickBack = onBackClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun CancelReservationTopBarPreview() {
    PicplzTheme {
        CancelReservationTopBar(
            onBackClick = {},
        )
    }
}
