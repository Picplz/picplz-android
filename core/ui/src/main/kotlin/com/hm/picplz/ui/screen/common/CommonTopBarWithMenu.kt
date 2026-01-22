package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

private object CommonTopBarWithMenuDefaults {
    val Height = 44.dp
    val HorizontalPadding = 16.dp
    val IconSize = 18.dp
}

@Composable
fun CommonTopBarWithMenu(
    text: String,
    onClickBack: () -> Unit,
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(CommonTopBarWithMenuDefaults.Height)
                .padding(horizontal = CommonTopBarWithMenuDefaults.HorizontalPadding),
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
                    modifier = Modifier.size(CommonTopBarWithMenuDefaults.IconSize),
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
                    modifier = Modifier.size(CommonTopBarWithMenuDefaults.IconSize),
                )
            }
        }
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
