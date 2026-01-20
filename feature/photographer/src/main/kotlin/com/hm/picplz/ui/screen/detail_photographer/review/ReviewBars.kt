package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.data.model.KeywordBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ReviewBars(items: List<KeywordBar>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(39.dp)
                    .background(MainThemeColor.Gray1, RoundedCornerShape(5.dp))
                    .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(5.dp))
            ) {
                // value에 따라 너비가 결정되는 박스
                // TODO: 박스 채우는 기준에 따라 로직 변경 필요
                Box(
                    modifier = Modifier
                        .width((item.count * 10).dp) // value 값에 따라 width 동적 적용
                        .height(39.dp)
                        .background(MainThemeColor.Olive, RoundedCornerShape(5.dp))
                )

                Image(
                    painter = rememberAsyncImagePainter(model = item.icon),
                    contentDescription = "리뷰 아이콘",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                        .padding(start = 9.dp)
                )

                Text(
                    text = item.label,
                    color = MainThemeColor.Black,
                    style = pretendardTypography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 30.dp)
                )

                Text(
                    text = item.count.toString(),
                    color = MainThemeColor.Gray4,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}