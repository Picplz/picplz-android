package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

enum class PhotographerStatus(val label: String) {
    ENABLED("예약 가능"),
    DISABLED("예약 불가"),
    ASAP("바로 촬영"),
}

@Composable
fun CommonStatusText(
    modifier: Modifier = Modifier,
    type: PhotographerStatus = PhotographerStatus.DISABLED
) {
    val iconRes = when (type) {
        PhotographerStatus.DISABLED -> R.drawable.inactive_dot
        PhotographerStatus.ENABLED,
        PhotographerStatus.ASAP -> R.drawable.tag_circle
    }
    val textColor = when (type) {
        PhotographerStatus.DISABLED -> MainThemeColor.Gray3
        PhotographerStatus.ENABLED,
        PhotographerStatus.ASAP -> MainThemeColor.Green120
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "tag",
            modifier = Modifier.size(10.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = type.label, style = MainThemeFont.InnerTag, color = textColor)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonStatusTextPreview() {
    PicplzTheme {
        CommonStatusText(
            type = PhotographerStatus.DISABLED
        )
    }
}