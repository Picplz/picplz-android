package com.hm.picplz.ui.screen.chat_room.composable.bubble

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun ChatBubbleButton(
    onClick: () -> Unit,
    text: String,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(0.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MainThemeColor.Black,
            contentColor = MainThemeColor.White
        ),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MainFontFamily.buttonChat
        )
    }
}