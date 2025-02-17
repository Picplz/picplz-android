package com.hm.picplz.ui.screen.common.common_chip

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    height: Dp = 21.dp,
    horizontalPadding: Dp = 7.dp,
    verticalPadding: Dp = 2.dp,
    backgroundColor: Color = MainThemeColor.Black,
    textColor: Color = MainThemeColor.White,
    radius: Dp = 5.dp,
    @DrawableRes iconResId: Int? = null,
    location: String = "left",
    gap: Dp = 4.dp,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(radius),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontalPadding, verticalPadding),
        modifier = Modifier.height(height)
    ) {

        if (location == "left" && iconResId != null) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "icon Button iamge",
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.width(gap))
        }

        Text(
            text = label,
            style = pretendardTypography.bodySmall.copy(color = textColor)
        )

        if (location == "right" && iconResId != null) {
            Spacer(modifier = Modifier.width(gap))

            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "icon Button iamge",
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonIconButtonPreview() {
    PicplzTheme {
        CommonIconButton(
            label = "팔로우",
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