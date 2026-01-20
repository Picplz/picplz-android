package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

enum class BadgeTheme {
    ACTIVE,
    INACTIVE,
    DISABLED,
}

@Composable
fun CommonBadge(
    modifier: Modifier = Modifier,
    label: String = "",
    theme: BadgeTheme = BadgeTheme.ACTIVE,
) {
    val (backgroundColor, textColor) =
        when (theme) {
            BadgeTheme.ACTIVE -> Pair(Color(0xFFEBF9DD), MainThemeColor.Green120)
            BadgeTheme.INACTIVE -> Pair(Color(0xFFFAE5E5), Color(0xFFDE4F69))
            BadgeTheme.DISABLED -> Pair(MainThemeColor.Gray2, MainThemeColor.Gray4)
        }

    val fontWeight =
        if (theme == BadgeTheme.DISABLED) FontWeight.Normal else FontWeight.SemiBold

    Row(
        modifier =
            modifier
                .height(20.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = pretendardTypography.labelMedium,
            color = textColor,
            fontWeight = fontWeight,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonBadgeActivePreview() {
    PicplzTheme {
        CommonBadge(
            label = "예약 확정",
            theme = BadgeTheme.ACTIVE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonBadgeInactivePreview() {
    PicplzTheme {
        CommonBadge(
            label = "예약 대기",
            theme = BadgeTheme.INACTIVE,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonBadgeDisabledPreview() {
    PicplzTheme {
        CommonBadge(
            label = "인스타 종합 패키지",
            theme = BadgeTheme.DISABLED,
        )
    }
}
