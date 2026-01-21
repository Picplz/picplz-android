package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    text: String,
    onClickBack: () -> Unit,
    showMenuIcon: Boolean = false,
    onClickMenu: () -> Unit = {},
    textStyle: TextStyle = pretendardTypography.bodyMedium,
    paddingStart: Dp = 0.dp,
    iconSize: Dp = 16.dp,
    spacerWidth: Dp = 50.dp,
) {
    Box(
        modifier =
            Modifier
                .height(44.dp)
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = onClickBack,
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(start = paddingStart),
            ) {
                Image(
                    painter = painterResource(R.drawable.triangle_left),
                    contentDescription = "arrow left",
                    modifier = Modifier.size(iconSize),
                )
            }
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    style = textStyle,
                )
            }
            Box(
                modifier =
                    Modifier
                        .size(spacerWidth)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                if (showMenuIcon) {
                    IconButton(onClick = onClickMenu) {
                        Icon(
                            modifier =
                                Modifier
                                    .size(18.dp),
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "상단바 메뉴 아이콘",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommonTopBarWithSubtitle(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    onClickBack: () -> Unit,
    showMenuIcon: Boolean = false,
    onClickMenu: () -> Unit = {},
    textStyle: TextStyle = pretendardTypography.bodyMedium,
    subTextStyle: TextStyle = MainFontFamily.caption,
    paddingStart: Dp = 0.dp,
    iconSize: Dp = 16.dp,
    spacerWidth: Dp = 50.dp,
) {
    Box(
        modifier =
            Modifier
                .height(44.dp)
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = onClickBack,
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(start = paddingStart),
            ) {
                Image(
                    painter = painterResource(R.drawable.triangle_left),
                    contentDescription = "arrow left",
                    modifier = Modifier.size(iconSize),
                )
            }
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = text,
                        style = textStyle,
                    )
                    Text(
                        text = subText,
                        style = subTextStyle,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
            Box(
                modifier =
                    Modifier
                        .size(spacerWidth)
                        .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                if (showMenuIcon) {
                    IconButton(onClick = onClickMenu) {
                        Icon(
                            modifier =
                                Modifier
                                    .size(18.dp),
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "상단바 메뉴 아이콘",
                        )
                    }
                }
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
                .height(44.dp),
    ) {
        CommonTopBar(
            text = title,
            onClickBack = { onClickBack() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarPreview() {
    PicplzTheme {
        CommonTopBar(
            text = "제목",
            onClickBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarWithSubtitlePreview() {
    PicplzTheme {
        CommonTopBarWithSubtitle(
            text = "제목",
            subText = "부제목",
            subTextStyle = MainFontFamily.caption.copy(color = MainThemeColor.Green120),
            onClickBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarWithSubtitleMenuPreview() {
    PicplzTheme {
        CommonTopBarWithSubtitle(
            text = "제목",
            subText = "부제목",
            subTextStyle = MainFontFamily.caption.copy(color = MainThemeColor.Green120),
            onClickBack = {},
            showMenuIcon = true,
        )
    }
}
