package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

private object CommonBottomButtonDefaults {
    val VerticalPadding = 14.dp
    val HorizontalPadding = 20.dp
    val CornerRadius = 5.dp
}

@Composable
fun CommonBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.Black,
                contentColor = MainThemeColor.White,
                disabledContainerColor = MainThemeColor.Gray3,
                disabledContentColor = MainThemeColor.Gray2,
            ),
        shape = RoundedCornerShape(CommonBottomButtonDefaults.CornerRadius),
        contentPadding =
            PaddingValues(
                vertical = CommonBottomButtonDefaults.VerticalPadding,
                horizontal = CommonBottomButtonDefaults.HorizontalPadding,
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = pretendardTypography.labelLarge,
        )
    }
}

@Composable
fun CommonBottomOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.White,
                contentColor = MainThemeColor.Black,
            ),
        shape = RoundedCornerShape(CommonBottomButtonDefaults.CornerRadius),
        border = BorderStroke(1.dp, MainThemeColor.Gray3),
        contentPadding =
            PaddingValues(
                vertical = CommonBottomButtonDefaults.VerticalPadding,
                horizontal = CommonBottomButtonDefaults.HorizontalPadding,
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = pretendardTypography.labelLarge,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomButtonEnabledPreview() {
    PicplzTheme {
        CommonBottomButton(
            text = "다음",
            onClick = {},
            enabled = true,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomButtonDisabledPreview() {
    PicplzTheme {
        CommonBottomButton(
            text = "다음",
            onClick = {},
            enabled = false,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomOutlinedButtonPreview() {
    PicplzTheme {
        CommonBottomOutlinedButton(
            text = "다음",
            onClick = {},
            enabled = true,
        )
    }
}
