package com.hm.picplz.ui.screen.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.toArgb
import android.graphics.BlurMaskFilter
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath

@Composable
fun CommonToggleSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
    checkedTrackColor: Color = MainThemeColor.Green120,
    checkedBorderColor: Color = MainThemeColor.Green100,
    uncheckedTrackColor: Color = Color(0xFFE0E0E0),
    uncheckedBorderColor: Color = MainThemeColor.Gray2,
    borderWidth: Dp = 1.dp,
    thumbColor: Color = MainThemeColor.White,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val width = 60.dp
    val height = 25.dp
    val thumbSize = 19.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - 2.dp else 2.dp,
        label = "thumbOffset"
    )

    val innerShadowPaint = remember {
        Paint().apply {
            color = Color.Black.copy(alpha = 0.35f)
            style = PaintingStyle.Stroke
            strokeWidth = 5f
            isAntiAlias = true
        }
    }

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .border(
                width = borderWidth,
                color = if (checked) checkedBorderColor else uncheckedBorderColor,
                shape = RoundedCornerShape(height / 2)
            )
            .background(if (checked) checkedTrackColor else uncheckedTrackColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = 24.dp),
                onClick = { onCheckedChange(!checked) }
            )
            .drawWithCache {
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            cornerRadius = CornerRadius(size.height / 2, size.height / 2)
                        )
                    )
                }

                onDrawWithContent {
                    drawContent()
                    drawIntoCanvas { canvas ->
                        innerShadowPaint.asFrameworkPaint().apply {
                            maskFilter = BlurMaskFilter(2.dp.toPx(), BlurMaskFilter.Blur.NORMAL)
                        }

                        canvas.nativeCanvas.save()
                        canvas.nativeCanvas.clipPath(path.asAndroidPath())

                        canvas.drawPath(path, innerShadowPaint)
                        canvas.nativeCanvas.restore()
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomToggleSwitchCheckedPreview() {
    var checked by remember { mutableStateOf(true) }

    CommonToggleSwitch(
        checked = checked,
        onCheckedChange = { checked = it }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomToggleSwitchUncheckedPreview() {
    var checked by remember { mutableStateOf(false) }

    CommonToggleSwitch(
        checked = checked,
        onCheckedChange = { checked = it }
    )
}