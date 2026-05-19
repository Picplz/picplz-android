package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@Composable
fun CommonBackgroundTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    textStyle: TextStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
    placeholderStyle: TextStyle = MainThemeFont.Body.copy(color = MainThemeColor.Gray3),
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(5.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(MainThemeColor.Gray1, shape)
                .border(1.dp, MainThemeColor.Gray2, shape),
        singleLine = singleLine,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        enabled = enabled,
        decorationBox = { innerTextField ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isBlank() && placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        style = placeholderStyle,
                    )
                }
                innerTextField()
            }
        },
    )
}
