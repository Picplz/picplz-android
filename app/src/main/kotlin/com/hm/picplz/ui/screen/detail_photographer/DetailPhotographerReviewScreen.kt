package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.detail_photographer.Review.ReviewBars
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.utils.ReviewUtil

@Composable
fun DetailPhotographerReviewScreen(navController: NavController) {
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

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


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(MainThemeColor.White) // 배경을 주어 내용이 비치지 않도록
                        .zIndex(1f)
                        .height(56.dp)
                ) {
                    CommonTopBar(
                        text = "리뷰",
                        onClickBack = { /*TODO*/ }
                    )
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()) // verticalScroll을 적용
                ) {
                    Column(modifier = paddingModifier) {

                        Row {
                            Text(text = "촬영 만족도", style = buttonText)
                            Spacer(modifier = Modifier.width(4.dp))
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
                        }

                        Spacer(modifier = Modifier.height(11.dp))

                        ReviewBars(items = items)

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "리뷰", style = buttonText)
                            Spacer(modifier = Modifier.width(4.dp)) // 텍스트 사이에 간격 추가
                            Text(text = "32")
                        }
                    }

                    Divider(
                        color = MainThemeColor.Gray2,
                        thickness = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    )


                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerReviewScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerReviewScreen(navController = navController)
    }
}