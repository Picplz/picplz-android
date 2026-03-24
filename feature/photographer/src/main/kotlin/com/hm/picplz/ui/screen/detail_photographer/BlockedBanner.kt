package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.photographer.R
import com.hm.picplz.ui.theme.MainThemeFont

private val BannerRed = Color(0xFFE53935)

@Composable
fun BlockedBanner(
    onUnblock: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(BannerRed)
                .padding(horizontal = 15.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.blocked_account_message),
            style = MainThemeFont.BodyBold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.unblock_button),
            style = MainThemeFont.Body,
            color = Color.White,
            modifier = Modifier.clickable { onUnblock() },
        )
    }
}
