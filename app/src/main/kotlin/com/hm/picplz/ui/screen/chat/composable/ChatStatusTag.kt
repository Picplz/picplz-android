package com.hm.picplz.ui.screen.chat.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatStatusTag(
    modifier: Modifier = Modifier,
    label: String,
    onClick: (() -> Unit)? = null,
    isActive: Boolean? = false
) {
    Box(
        modifier = modifier
            .height(25.dp)
            .border(
                width = 1.dp,
                color = if (isActive == true) MainThemeColor.Black else MainThemeColor.Gray2,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = if (isActive == true) MainThemeColor.Black else MainThemeColor.Gray1,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                        onClick = onClick
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MainThemeFont.InnerTag,
            fontWeight = if (isActive == false) FontWeight.Normal else MainThemeFont.InnerTag.fontWeight,
            color = if (isActive == true) MainThemeColor.White else MainThemeColor.Gray4
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTagTruePreview() {
    PicplzTheme {
        ChatStatusTag(
            label = "예약 대기",
            isActive = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTagFalsePreview() {
    PicplzTheme {
        ChatStatusTag(
            label = "예약 확정",
        )
    }
}