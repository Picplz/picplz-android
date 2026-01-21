package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun CommonTopBarWithSubtitle(
    text: String,
    subText: String,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    subTextStyle: TextStyle = MainFontFamily.caption,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(TopBarHeight)
                .padding(horizontal = TopBarHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(onClick = onClickBack) {
                Image(
                    painter = painterResource(R.drawable.triangle_left),
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(TopBarIconSize),
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    style = pretendardTypography.bodyMedium,
                )
                Text(
                    text = subText,
                    style = subTextStyle,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            IconButton(onClick = onClickMenu) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "메뉴",
                    modifier = Modifier.size(TopBarIconSize),
                )
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonTopBarWithSubtitlePreview() {
    PicplzTheme {
        CommonTopBarWithSubtitle(
            text = "유가영 작가",
            subText = "당장 촬영 가능",
            subTextStyle = MainFontFamily.caption.copy(color = MainThemeColor.Green120),
            onClickBack = {},
            onClickMenu = {},
        )
    }
}
