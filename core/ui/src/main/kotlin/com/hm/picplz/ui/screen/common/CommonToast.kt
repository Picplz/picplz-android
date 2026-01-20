package com.hm.picplz.ui.screen.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import kotlinx.coroutines.delay

enum class ToastPosition {
    TOP,
    CENTER,
    BOTTOM,
}

@Composable
fun CommonToast(
    modifier: Modifier = Modifier,
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    position: ToastPosition = ToastPosition.BOTTOM,
    offset: Dp = 26.dp,
) {
    var showToast by remember(isVisible) { mutableStateOf(isVisible) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            showToast = true
            delay(2000)
            showToast = false
            delay(300)
            onDismiss()
        }
    }

    val alignment =
        when (position) {
            ToastPosition.TOP -> Alignment.TopCenter
            ToastPosition.CENTER -> Alignment.Center
            ToastPosition.BOTTOM -> Alignment.BottomCenter
        }

    val (slideIn, slideOut) =
        when (position) {
            ToastPosition.TOP ->
                Pair(
                    slideInVertically { -it },
                    slideOutVertically { -it },
                )
            ToastPosition.CENTER ->
                Pair(
                    slideInVertically { it / 2 },
                    slideOutVertically { it / 2 },
                )
            ToastPosition.BOTTOM ->
                Pair(
                    slideInVertically { it },
                    slideOutVertically { it },
                )
        }

    AnimatedVisibility(
        visible = showToast,
        enter = slideIn + fadeIn(),
        exit = slideOut + fadeOut(),
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 17.dp)
                    .then(
                        when (position) {
                            ToastPosition.BOTTOM -> Modifier.padding(bottom = offset)
                            ToastPosition.TOP -> Modifier.padding(top = offset)
                            ToastPosition.CENTER -> Modifier
                        },
                    ),
            contentAlignment = alignment,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Black.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Text(
                    text = message,
                    modifier = Modifier.fillMaxWidth(),
                    style = pretendardTypography.bodyMedium,
                    color = MainThemeColor.White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
