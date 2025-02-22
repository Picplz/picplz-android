package com.hm.picplz.ui.screen.detail_photographer.Review

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.screen.detail_photographer.ReviewItem
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ReviewBars(items: List<ReviewItem>, modifier: Modifier = Modifier) {
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
                Box(
                    modifier = Modifier
                        .width((item.value * 10).dp) // value 값에 따라 width 동적 적용
                        .height(39.dp)
                        .background(MainThemeColor.Olive, RoundedCornerShape(5.dp))
                )

                Image(
                    painterResource(id = item.imageUri),
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
                    text = item.value.toString(),
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