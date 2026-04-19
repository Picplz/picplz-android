package com.hm.picplz.ui.screen.my_page.toggleSwitch

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 56.dp,
    height: Dp = 28.dp,
    thumbSize: Dp = 20.dp,
    checkedTrackColor: Color = MainThemeColor.Green120,
    checkedBorderColor: Color = MainThemeColor.Green100,
    checkedInnerTrackColor: Color = MainThemeColor.Green100,
    uncheckedTrackColor: Color = MainThemeColor.Gray2,
    uncheckedBorderColor: Color = MainThemeColor.Gray3,
    thumbColor: Color = MainThemeColor.White,
    thumbHorizontalInset: Dp = 4.73.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - thumbHorizontalInset else thumbHorizontalInset,
        label = "thumbOffset",
    )

    val innerShadowPaint =
        remember {
            Paint().apply {
                color = Color.Black.copy(alpha = 0.28f)
                style = PaintingStyle.Stroke
                strokeWidth = 5f
                isAntiAlias = true
            }
        }

    Box(
        modifier =
            modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .border(
                    width = 1.dp,
                    color = if (checked) checkedBorderColor else uncheckedBorderColor,
                    shape = RoundedCornerShape(height / 2),
                )
                .background(if (checked) checkedTrackColor else uncheckedTrackColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = false, radius = 24.dp),
                    onClick = { onCheckedChange(!checked) },
                )
                .drawWithCache {
                    val path =
                        Path().apply {
                            addRoundRect(
                                RoundRect(
                                    left = 0f,
                                    top = 0f,
                                    right = size.width,
                                    bottom = size.height,
                                    cornerRadius = CornerRadius(size.height / 2, size.height / 2),
                                ),
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
        contentAlignment = Alignment.CenterStart,
    ) {
        if (checked) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clip(RoundedCornerShape((height - 4.dp) / 2))
                        .background(checkedInnerTrackColor.copy(alpha = 0.55f)),
            )
        }

        Box(
            modifier =
                Modifier
                    .size(thumbSize)
                    .offset(x = thumbOffset)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false,
                    )
                    .clip(CircleShape)
                    .background(thumbColor),
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
            onCheckedChange = { isToggled = it },
        )
    }
}
