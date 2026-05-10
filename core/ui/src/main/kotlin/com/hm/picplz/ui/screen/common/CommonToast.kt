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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import kotlinx.coroutines.delay

private object CommonToastDefaults {
    val Height = 45.dp
    val HorizontalPadding = 15.dp
    val ContentPadding = 16.dp
    val CornerRadius = 50.dp
    val BottomOffset = 50.dp
    const val BACKGROUND_ALPHA = 0.6f
    const val DURATION_MS = 2000L
}

@Composable
fun CommonToast(
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    bottomOffset: Dp = CommonToastDefaults.BottomOffset,
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(CommonToastDefaults.DURATION_MS)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut(),
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(horizontal = CommonToastDefaults.HorizontalPadding)
                    .padding(bottom = bottomOffset),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(CommonToastDefaults.Height)
                        .background(
                            color = MainThemeColor.Black.copy(alpha = CommonToastDefaults.BACKGROUND_ALPHA),
                            shape = RoundedCornerShape(CommonToastDefaults.CornerRadius),
                        )
                        .padding(horizontal = CommonToastDefaults.ContentPadding),
                contentAlignment =
                    if (textAlign == TextAlign.Start) Alignment.CenterStart else Alignment.Center,
            ) {
                Text(
                    text = message,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.White,
                    textAlign = textAlign,
                )
            }
        }
    }
}
