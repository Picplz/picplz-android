package com.hm.picplz.ui.screen.order_detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.theme.MainThemeColor.Black
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun PhotographerSection(
    photographerName: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.order_detail_section_photographer_type),
            style = MainThemeFont.ButtonDefault,
            color = Black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = photographerName,
            style = MainThemeFont.Body,
            color = Black,
        )
    }
}
