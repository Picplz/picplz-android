package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

private val ToastBackground = Color(0xFF0C0C0C).copy(alpha = 0.7f)
private const val TOAST_DURATION_MS = 2000L

/**
 * 커스텀 토스트 오버레이.
 * message가 null이 아니면 하단에 표시, 2초 후 onDismiss 호출.
 */
@Composable
fun CommonToast(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMs: Long = TOAST_DURATION_MS,
) {
    LaunchedEffect(message) {
        if (message != null) {
            kotlinx.coroutines.delay(durationMs)
            onDismiss()
        }
    }

    message?.let {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Text(
                text = it,
                style = MainThemeFont.Body,
                color = MainThemeColor.White,
                modifier =
                    Modifier
                        .background(
                            color = ToastBackground,
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 64.dp, vertical = 13.dp),
            )
        }
    }
}
