package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

private object QuickShootBottomButtonDefaults {
    val VerticalPadding = 14.dp
    val HorizontalPadding = 20.dp
    val CornerRadius = 5.dp
}

@Composable
fun QuickShootBottomButton(
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
                containerColor = MainThemeColor.SlateBlue,
                contentColor = MainThemeColor.White,
                disabledContainerColor = MainThemeColor.Gray3,
                disabledContentColor = MainThemeColor.Gray2,
            ),
        shape = RoundedCornerShape(QuickShootBottomButtonDefaults.CornerRadius),
        contentPadding =
            PaddingValues(
                vertical = QuickShootBottomButtonDefaults.VerticalPadding,
                horizontal = QuickShootBottomButtonDefaults.HorizontalPadding,
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = pretendardTypography.labelLarge,
        )
    }
}
