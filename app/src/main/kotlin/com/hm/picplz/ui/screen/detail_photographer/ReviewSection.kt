package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hm.picplz.R
import com.hm.picplz.ui.screen.detail_photographer.Review.ReviewBars
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.utils.ReviewUtil

data class ReviewItem(
    val imageUri: Int, // 이미지 URI (리소스 ID)
    val label: String, // 텍스트 레이블
    val value: Int // 값 (예: "12"과 같은 숫자)
)

@Composable
fun ReviewSection(modifier: Modifier, navController: NavHostController) {
    // 아이템 리스트 정의
    val items = listOf(
        ReviewItem(
            imageUri = R.drawable.default_profile,
            label = "사진을 예쁘게 찍어줘요",
            value = 14
        ),
        ReviewItem(
            imageUri = R.drawable.user_selected,
            label = "포즈 추천을 잘 해줘요",
            value = 10
        ),
        ReviewItem(
            imageUri = R.drawable.center_char,
            label = "친절해요",
            value = 9
        ),
        ReviewItem(
            imageUri = R.drawable.user_deselected,
            label = "보정을 잘 해요",
            value = 5
        ),
    )

    val totalRating = 3.5
    val starList = ReviewUtil.calculateStarRating(totalRating) // MathUtil에서 호출

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "촬영 만족도", style = buttonText)
        Spacer(modifier = Modifier.height(10.dp))

        // 별점 부분
        Row(verticalAlignment = Alignment.CenterVertically) {
            starList.forEach { star ->
                Image(
                    painter = painterResource(id = star),
                    contentDescription = "별점"
                )
            }
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = totalRating.toString(),
                style = pretendardTypography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MainThemeColor.Gray4
            )
        }

        Spacer(modifier = Modifier.height(17.dp))

        // 아이템 리스트를 돌면서 표시
        ReviewBars(items = items, modifier = modifier)

        Row(modifier = modifier
            .clickable { navController.navigate("review-photographer") }
            .align(Alignment.End)
        ) {
            Text(
                "전체 리뷰 보러가기 (00)",
                style = pretendardTypography.bodyMedium.copy(
                    color = MainThemeColor.Gray4
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(id = R.drawable.depth_arrow),
                contentDescription = "depth_arrow",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}