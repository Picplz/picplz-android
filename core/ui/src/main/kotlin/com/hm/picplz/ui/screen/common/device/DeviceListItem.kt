package com.hm.picplz.ui.screen.common.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun DeviceListItem(
    companyName: String,
    deviceName: String,
    removeContentDescription: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Gray2,
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(horizontal = 15.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = companyName,
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = deviceName,
                style = MainFontFamily.bodyBold,
                color = MainThemeColor.Gray5,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.close_circle),
            contentDescription = removeContentDescription,
            modifier =
                Modifier
                    .size(20.dp)
                    .clickable { onRemove() },
        )
    }
}
