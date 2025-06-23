package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun DeviceSelectorBox(
    modifier: Modifier = Modifier,
    text: String?,
    placeholder: String,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(
                width = 1.dp,
                color = when {
                    !enabled -> MainThemeColor.Gray2
                    isSelected -> MainThemeColor.Black
                    else -> Color(0xFFB0BCC4)
                },
                RoundedCornerShape(5.dp)
            )
            .background(
                color = if (enabled) MainThemeColor.White else MainThemeColor.Gray1,
                RoundedCornerShape(5.dp)
            )
            .let { boxModifier ->
                if (enabled) {
                    boxModifier.clickable { onClick() }
                } else {
                    boxModifier
                }
            }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text ?: placeholder,
                style = pretendardTypography.bodyMedium,
                color = when {
                    !enabled -> MainThemeColor.Gray3
                    text == null -> MainThemeColor.Gray
                    else -> MainThemeColor.Black
                }
            )
            Image(
                painter = painterResource(id = R.drawable.triangle_down),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                alpha = if (enabled) 1f else 0.3f
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceSelectorBoxPreview() {
    PicplzTheme {
        DeviceSelectorBox(
            text = "카메라",
            placeholder = "카메라를 선택해주세요",
            onClick = {},
            isSelected = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceSelectorBoxNotSelectedPreview() {
    PicplzTheme {
        DeviceSelectorBox(
            text = "카메라",
            placeholder = "카메라를 선택해주세요",
            onClick = {},
            isSelected = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceSelectorBoxDisabledPreview() {
    PicplzTheme {
        DeviceSelectorBox(
            text = "카메라",
            placeholder = "카메라를 선택해주세요",
            onClick = {},
            isSelected = true,
            enabled = false,
        )
    }
}