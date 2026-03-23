package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

private object CommonTopBarDefaults {
    val Height = 44.dp
    val IconSize = 18.dp
}

@Composable
fun CommonTopBar(
    text: String,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(CommonTopBarDefaults.Height),
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
                    modifier = Modifier.size(CommonTopBarDefaults.IconSize),
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
            Row { actions() }
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
                .height(CommonTopBarDefaults.Height),
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
