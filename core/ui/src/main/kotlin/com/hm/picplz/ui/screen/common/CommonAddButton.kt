package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun CommonAddButton(
    modifier: Modifier = Modifier,
    text: String = "추가하기",
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp),
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MainThemeColor.White,
            contentColor = MainThemeColor.Gray5
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MainThemeColor.Gray3
        )
    ) {
        Text(
            text = text,
            style = pretendardTypography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonAddButtonPreview() {
    PicplzTheme {
        CommonAddButton(
            text = "추가하기",
            onClick = {}
        )
    }
}