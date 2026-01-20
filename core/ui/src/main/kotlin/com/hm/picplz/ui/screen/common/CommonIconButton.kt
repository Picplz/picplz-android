package com.hm.picplz.ui.screen.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun CommonIconButton(
    modifier: Modifier = Modifier,
    label: String = "",
    horizontalPadding: Dp = 7.dp,
    verticalPadding: Dp = 2.dp,
    backgroundColor: Color = MainThemeColor.Black,
    textColor: Color = MainThemeColor.White,
    textStyle: TextStyle = pretendardTypography.bodySmall,
    borderRadius: Dp = 0.dp,
    @DrawableRes iconResId: Int? = null,
    iconSize: Dp = 8.dp,
    location: String = "left",
    gap: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier =
        onClick?.let {
            Modifier.clickable { it() }
        } ?: Modifier // onClick이 null일 경우 clickable modifier를 적용하지 않음

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(borderRadius))
                .then(clickableModifier)
                .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontalPadding, verticalPadding),
        ) {
            if (location == "left" && iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "icon Button image",
                    modifier = Modifier.size(iconSize),
                )
                Spacer(modifier = Modifier.width(gap))
            }

            Text(
                text = label,
                color = textColor,
                style = textStyle,
            )

            if (location == "right" && iconResId != null) {
                Spacer(modifier = Modifier.width(gap))
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "icon Button image",
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
            location = "right",
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
            location = "left",
        )
    }
}
