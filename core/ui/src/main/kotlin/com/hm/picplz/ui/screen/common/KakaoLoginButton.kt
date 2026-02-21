package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

private object KakaoLoginButtonDefaults {
    val VerticalPadding = 14.dp
    val HorizontalPadding = 20.dp
    val CornerRadius = 5.dp
    val IconSize = 21.dp
}

@Composable
fun KakaoLoginButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.Yellow,
                contentColor = MainThemeColor.Black,
            ),
        shape = RoundedCornerShape(KakaoLoginButtonDefaults.CornerRadius),
        contentPadding =
            PaddingValues(
                vertical = KakaoLoginButtonDefaults.VerticalPadding,
                horizontal = KakaoLoginButtonDefaults.HorizontalPadding,
            ),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.kakao),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(KakaoLoginButtonDefaults.IconSize),
            )
            Text(
                text = text,
                style = pretendardTypography.labelLarge,
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun KakaoLoginButtonPreview() {
    PicplzTheme {
        KakaoLoginButton(
            text = "카카오로 계속하기",
            onClick = {},
        )
    }
}
