package com.hm.picplz.ui.screen.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "검색어를 입력하세요",
    onSearchClick: (() -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Search,
    keyboardActions: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(50.dp))
                .border(
                    width = 1.dp,
                    color = if (isFocused) MainThemeColor.Olive else MainThemeColor.Gray6,
                    shape = RoundedCornerShape(50.dp),
                )
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            onFocusChanged?.invoke(focusState.isFocused)
                        },
                enabled = enabled,
                textStyle = MainThemeFont.Body.copy(color = MainThemeColor.Black),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = imeAction,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            keyboardActions?.invoke()
                            focusManager.clearFocus()
                        },
                        onDone = {
                            keyboardActions?.invoke()
                            focusManager.clearFocus()
                        },
                    ),
                cursorBrush = SolidColor(MainThemeColor.Black),
                singleLine = true,
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MainThemeFont.Body,
                            color = MainThemeColor.Gray3,
                        )
                    }
                    inner()
                },
            )

            if (value.isNotEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MainThemeColor.Gray2)
                            .clickable {
                                onValueChange("")
                                focusManager.clearFocus()
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "clear",
                        Modifier.size(13.dp),
                        tint = MainThemeColor.Gray4,
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "검색",
                modifier =
                    Modifier
                        .size(14.dp)
                        .clickable {
                            onSearchClick?.invoke()
                                ?: keyboardActions?.invoke()
                            focusManager.clearFocus()
                        },
                tint = if (isFocused) MainThemeColor.Olive else MainThemeColor.Black,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFieldPreview() {
    PicplzTheme {
        SearchField(
            value = "",
            onValueChange = {},
            placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchFieldWithValuePreview() {
    PicplzTheme {
        SearchField(
            value = "연남동",
            onValueChange = {},
            placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
            modifier = Modifier.padding(16.dp),
        )
    }
}
