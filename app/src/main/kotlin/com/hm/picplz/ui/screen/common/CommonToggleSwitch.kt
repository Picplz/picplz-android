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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun CommonToggleSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
    checkedTrackColor: Color = MainThemeColor.Green120,
    uncheckedTrackColor: Color = Color(0xFFE0E0E0),
    thumbColor: Color = Color.White,
    checkedBorderColor: Color = MainThemeColor.Green100,
    uncheckedBorderColor: Color = MainThemeColor.Gray2,
    borderWidth: Dp = 1.dp,
    shadowElevation: Dp = 2.dp
) {
    val interactionSource = remember { MutableInteractionSource() }

    val width = 60.dp
    val height = 25.dp

    val thumbSize = 19.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - 2.dp else 2.dp,
        label = "thumbOffset"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .shadow(
                elevation = shadowElevation / 2,
                shape = RoundedCornerShape(height / 2)
            )
            .clip(RoundedCornerShape(height / 2))
            .border(
                width = borderWidth,
                color = if (checked) checkedBorderColor else uncheckedBorderColor,
                shape = RoundedCornerShape(height / 2)
            )
            .background(if (checked) checkedTrackColor else uncheckedTrackColor)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 24.dp),
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .shadow(
                    elevation = shadowElevation,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(thumbColor)
                .border(
                    width = borderWidth / 2,
                    color = uncheckedBorderColor,
                    shape = CircleShape
                )
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