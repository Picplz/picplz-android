package com.hm.picplz.ui.screen.common

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun CommonIconButton(
    label: String = "",
    horizontalPadding: Dp = 7.dp,
    verticalPadding: Dp = 2.dp,
    backgroundColor: Color = MainThemeColor.Black,
    textColor: Color = MainThemeColor.White,
    textStyle: TextStyle = pretendardTypography.bodySmall,
    borderRadius: Dp = 0.dp,
    @DrawableRes iconResId: Int? = null,
    location: String = "left",                // left || right
    gap: Dp = 4.dp,                           // 텍스트, 아이콘 사이의 간격
    onClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(borderRadius))
            .clickable { onClick() }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontalPadding, verticalPadding)
        ) {
            if (location == "left" && iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "icon Button image"
                )
                Spacer(modifier = Modifier.width(gap))
            }

            Text(
                text = label,
                color = textColor,
                style = textStyle
            )

            if (location == "right" && iconResId != null) {
                Spacer(modifier = Modifier.width(gap))
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "icon Button image"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonIconButtonPreview() {
    PicplzTheme {
        CommonIconButton(
            label = "추천순",
            textStyle = pretendardTypography.bodySmall.copy(color = MainThemeColor.Gray4),
            backgroundColor = MainThemeColor.Gray2,
            textColor = MainThemeColor.Gray4,
            iconResId = R.drawable.follow,
            location = "right"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonIconButtonPreview2() {
    PicplzTheme {
        CommonIconButton(
            label = "팔로잉",
            backgroundColor = MainThemeColor.Black,
            textColor = MainThemeColor.White,
            iconResId = R.drawable.following,
            location = "left"
        )
    }
}