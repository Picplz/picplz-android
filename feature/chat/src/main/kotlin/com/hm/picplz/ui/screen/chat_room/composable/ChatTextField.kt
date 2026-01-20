package com.hm.picplz.ui.screen.chat_room.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    inputText: String = "",
    onInputChanged: (String) -> Unit = {},
) {
    BasicTextField(
        value = inputText,
        onValueChange = { newValue -> onInputChanged(newValue) },
        modifier =
            modifier
                .height(40.dp)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Black,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(horizontal = 12.dp),
        textStyle = pretendardTypography.bodyMedium,
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (inputText.isEmpty()) {
                    Text(
                        text = "무엇이든 말해보세여",
                        style = pretendardTypography.bodyMedium,
                        color = MainThemeColor.Gray3,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun ChatTextFieldPreview() {
    PicplzTheme {
        ChatTextField()
    }
}
