package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.common_chip.CommonIconButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioSection(modifier: Modifier, portfolioPhotos: List<String>) {
    Column {
        // 포트폴리오
        Text(
            modifier = modifier.fillMaxWidth(),
            text = "포트폴리오",
            style = buttonText
        )

        // 3열의 고정 그리드를 직접 구성
        val chunkedImages = portfolioPhotos.take(9).chunked(3) // 3개씩 나눔, 최대 9개

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 7.dp)
        ) {
            chunkedImages.forEach { rowImages ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowImages.forEach { imageRes ->
                        Image(
                            painter = rememberAsyncImagePainter(model = imageRes),
                            contentDescription = "포트폴리오 이미지",
                            modifier = Modifier
                                .weight(1f) // 각 이미지가 동일한 크기를 가짐
                                .aspectRatio(1f) // 1:1 비율
                                .padding(2.dp),
                            contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                        )
                    }
                    // 남은 빈 공간 채우기
                    repeat(3 - rowImages.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        CommonIconButton(label = "포트폴리오 더보기",
            backgroundColor = Color.Transparent,
            textColor = MainThemeColor.Gray4,
            textStyle = pretendardTypography.bodyMedium,
            iconResId = R.drawable.depth_arrow,
            location = "right",
            horizontalPadding = 0.dp,
            verticalPadding = 0.dp,
            gap = 6.dp,
//        onClick = { navController.navigate("review-photographer") },
        modifier = modifier.align(Alignment.End)
        )

    }
}