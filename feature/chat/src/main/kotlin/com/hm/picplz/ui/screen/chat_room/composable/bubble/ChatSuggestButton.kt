package com.hm.picplz.ui.screen.chat_room.composable.bubble

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatSuggestButton(
    modifier: Modifier = Modifier,
    chatText: String,
    onClick: () -> Unit = {},
) {
    Button(
        modifier =
            modifier
                .height(30.dp),
        shape = RoundedCornerShape(5.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.Black,
                contentColor = MainThemeColor.White,
            ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick,
    ) {
        Text(
            text = chatText,
            style =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    lineHeight = 10.sp * 1.4,
                    letterSpacing = 0.sp,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatSuggestButtonPreview() {
    PicplzTheme {
        ChatSuggestButton(
            chatText = "촬영 소요 시간은 얼마나 걸리나요?",
        )
    }
}
