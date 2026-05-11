package com.hm.picplz.ui.screen.photographer_main.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun PhotographerDeviceSelectorBox(
    text: String?,
    placeholder: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isDirectInput: Boolean = false,
    inputText: String = "",
    onTextChange: (String) -> Unit = {},
) {
    if (isDirectInput) {
        val focusManager = LocalFocusManager.current

        BasicTextField(
            value = inputText,
            onValueChange = onTextChange,
            enabled = enabled,
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    },
                ),
            textStyle =
                pretendardTypography.bodyMedium.copy(
                    color = if (enabled) MainThemeColor.Black else MainThemeColor.Gray3,
                ),
            decorationBox = { innerTextField ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color =
                                    when {
                                        !enabled -> MainThemeColor.Gray2
                                        inputText.isNotEmpty() -> MainThemeColor.Black
                                        else -> MainThemeColor.Gray3
                                    },
                                RoundedCornerShape(5.dp),
                            )
                            .background(
                                color = MainThemeColor.Gray1,
                                RoundedCornerShape(5.dp),
                            )
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (inputText.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = pretendardTypography.bodyMedium,
                            color = MainThemeColor.Gray3,
                        )
                    }
                    innerTextField()
                }
            },
        )
    } else {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color =
                            when {
                                !enabled -> MainThemeColor.Gray2
                                isSelected -> MainThemeColor.Black
                                else -> MainThemeColor.Gray3
                            },
                        RoundedCornerShape(5.dp),
                    )
                    .background(
                        color = if (enabled) MainThemeColor.White else MainThemeColor.Gray1,
                        RoundedCornerShape(5.dp),
                    )
                    .let { boxModifier ->
                        if (enabled) {
                            boxModifier.clickable { onClick() }
                        } else {
                            boxModifier
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = text ?: placeholder,
                style = pretendardTypography.bodyMedium,
                color =
                    when {
                        !enabled -> MainThemeColor.Gray3
                        text == null -> MainThemeColor.Gray3
                        else -> MainThemeColor.Black
                    },
            )
        }
    }
}
