package com.hm.picplz.ui.screen.chat_room.composable.bubble

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.domain.model.MessageDirection
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun ChatBubbleSurface(
    direction: MessageDirection,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color =
            when (direction) {
                MessageDirection.SENT -> MainThemeColor.Gray1
                MessageDirection.RECEIVED -> MainThemeColor.White
            },
        border =
            when (direction) {
                MessageDirection.SENT -> null
                MessageDirection.RECEIVED -> BorderStroke(1.dp, MainThemeColor.Gray3)
            },
    ) {
        content()
    }
}
