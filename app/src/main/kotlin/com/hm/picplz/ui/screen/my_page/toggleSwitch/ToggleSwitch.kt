package com.hm.picplz.ui.screen.my_page.toggleSwitch

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

// TODO: 활성화되었을 때 디자인 적용
@Composable
fun ToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 50.dp,
    height: Dp = 25.dp,
    thumbSize: Dp = 18.dp,
    trackColor: Color = MainThemeColor.Gray2,
    trackStroke: Color = Color(0xFFE2E7EB),
    thumbColor: Color = Color.White,
    shadowOffsetX: Dp = 1.dp,
    shadowOffsetY: Dp = 1.dp,
    shadowBlur: Dp = 4.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.25f)
) {
    val padding = (height - thumbSize) / 2
    val targetX = if (checked) width - thumbSize - padding else padding
    val offsetX by animateDpAsState(targetX, spring(stiffness = Spring.StiffnessMedium))

    val density = LocalDensity.current
    val pxOffsetX = with(density) { offsetX.toPx() }
    val pxThumb = with(density) { thumbSize.toPx() }
    val pxShadowX = with(density) { shadowOffsetX.toPx() }
    val pxShadowY = with(density) { shadowOffsetY.toPx() }
    val pxBlur = with(density) { shadowBlur.toPx() }

    Box(
        modifier
            .size(width, height)
            .background(trackColor, RoundedCornerShape(height / 2))
            .border(1.dp, trackStroke, RoundedCornerShape(height / 2))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            Modifier
                .offset { IntOffset(pxOffsetX.toInt(), 0) }
                .size(thumbSize)
                .drawBehind {
                    val paint = Paint().apply {
                        color = shadowColor.toArgb()
                        maskFilter = BlurMaskFilter(pxBlur, BlurMaskFilter.Blur.NORMAL)
                    }
                    drawContext.canvas.nativeCanvas.drawCircle(
                        pxThumb / 2 + pxShadowX,
                        pxThumb / 2 + pxShadowY,
                        pxThumb / 2,
                        paint
                    )
                }
                .background(thumbColor, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleSwitchPreview() {
    var isToggled by remember { mutableStateOf(false) }

    PicplzTheme {
        ToggleSwitch(
            checked = isToggled,
            onCheckedChange = { isToggled = it }
        )
    }
}