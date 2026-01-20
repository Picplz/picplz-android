package com.hm.picplz.ui.screen.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ScheduleCardNone(
    modifier: Modifier = Modifier,
    mainText: String = "",
    subText: String = "",
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 20.dp,
    borderRadius: Dp = 5.dp,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(borderRadius))
            .border(1.dp, MainThemeColor.Gray3, RoundedCornerShape(borderRadius))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontalPadding, verticalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = mainText, style = MainThemeFont.ButtonDefault)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subText, style = MainThemeFont.Body, color = MainThemeColor.Gray4)
            }
            Image(
                painter = painterResource(id = R.drawable.triangle_right),
                contentDescription = "triangle_right"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleCardNonePreview() {
    PicplzTheme {
        ScheduleCardNone(
            mainText = "진행중인 촬영이 없어요",
            subText = "촬영지를 검색하고 작가들을 둘러보세요",
            onClick = {}
        )
    }
}
