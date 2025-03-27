package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainTypography
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.button

@Composable
fun CommonBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp),
    enabled: Boolean = true,
    containerColor: Color = MainThemeColor.Black,
    contentColor: Color = Color(0xFFFFFFFF),
    disabledContainerColor: Color = MainThemeColor.Gray3,
    disabledContentColor: Color = MainThemeColor.Gray2,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        shape = shape,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MainTypography.button,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonButtonPreview() {
    PicplzTheme {
        CommonBottomButton(text = "버튼", onClick = {})
    }
}