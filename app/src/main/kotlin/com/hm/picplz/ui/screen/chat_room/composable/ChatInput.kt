package com.hm.picplz.ui.screen.chat_room.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    onClickMenu: () -> Unit = {},
    onClickSend: () -> Unit = {},
    inputText: String = "",
    onInputChanged: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Black,
                    shape = CircleShape
                )
                .clickable{
                    onClickMenu()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = null,
                tint = MainThemeColor.Black
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        ChatTextField(
            modifier = Modifier
                .weight(1f),
            inputText = inputText,
            onInputChanged = onInputChanged,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Black,
                    shape = CircleShape
                )
                .clickable{
                    onClickSend()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.triangle_arrow_icon),
                contentDescription = null,
                tint = MainThemeColor.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatInputPreview() {
    PicplzTheme {
        ChatInput()
    }
}