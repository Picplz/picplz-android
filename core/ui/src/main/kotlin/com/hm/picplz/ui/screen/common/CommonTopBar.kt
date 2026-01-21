package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.zIndex
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

private val TopBarHeight = 44.dp
private val TopBarHorizontalPadding = 16.dp
private val IconSize = 18.dp

@Composable
fun CommonTopBar(
    text: String,
    onClickBack: () -> Unit,
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
                    modifier = Modifier.size(IconSize),
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = pretendardTypography.bodyMedium,
            )
        }

        Box(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CommonTopBarWithMenu(
    text: String,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
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
                    modifier = Modifier.size(IconSize),
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = pretendardTypography.bodyMedium,
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            IconButton(onClick = onClickMenu) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "메뉴",
                    modifier = Modifier.size(IconSize),
                )
            }
        }
    }
}

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
                    modifier = Modifier.size(IconSize),
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
                    modifier = Modifier.size(IconSize),
                )
            }
        }
    }
}

@Composable
fun CommonFixedTopBar(
    title: String,
    onClickBack: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .background(MainThemeColor.White)
                .zIndex(1f)
                .height(TopBarHeight),
    ) {
        CommonTopBar(
            text = title,
            onClickBack = onClickBack,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonTopBarPreview() {
    PicplzTheme {
        CommonTopBar(
            text = "제목",
            onClickBack = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonTopBarWithMenuPreview() {
    PicplzTheme {
        CommonTopBarWithMenu(
            text = "제목",
            onClickBack = {},
            onClickMenu = {},
        )
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
