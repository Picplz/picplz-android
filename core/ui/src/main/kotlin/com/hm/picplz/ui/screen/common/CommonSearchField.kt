package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CommonSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "검색어를 입력하세요",
    onSearchClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Search,
    keyboardActions: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(50.dp))
                .border(1.dp, MainThemeColor.Gray6, RoundedCornerShape(50.dp))
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                enabled = enabled,
                textStyle =
                    MaterialTheme.typography.bodyLarge.copy(
                        color = MainThemeColor.Black,
                    ),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = imeAction,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = { keyboardActions?.invoke() },
                        onDone = { keyboardActions?.invoke() },
                    ),
                cursorBrush = SolidColor(MainThemeColor.Black),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MainThemeColor.Gray3,
                        )
                    }
                    innerTextField()
                },
            )

            IconButton(
                onClick = {
                    onSearchClick?.invoke() ?: keyboardActions?.invoke()
                },
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "검색",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonSearchFieldPreview() {
    PicplzTheme {
        CommonSearchField(
            value = "",
            onValueChange = {},
            placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonSearchFieldWithValuePreview() {
    PicplzTheme {
        CommonSearchField(
            value = "연남동",
            onValueChange = {},
            placeholder = "동명(동, 면)으로 검색 (ex, 연남동)",
            modifier = Modifier.padding(16.dp),
        )
    }
}
