package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun DistanceText(
    distance: Long,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${distance}m",
        style = MainThemeFont.Body,
        color = MainThemeColor.Gray4,
        modifier = modifier,
    )
}
