package com.hm.picplz.ui.screen.detail_photographer.Review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hm.picplz.R
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.ui.screen.common.common_chip.CommonIconButton
import com.hm.picplz.ui.screen.detail_photographer.dummyPhotoReviews
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.utils.ReviewUtil
import com.hm.picplz.utils.SingleReviewType
import com.hm.picplz.utils.StarType

@Composable
fun SingleReview(
    navController: NavController,
    review: PhotographerReview,
    type: SingleReviewType = SingleReviewType.OVERVIEW,
    photoIndex: Int = 0
) {
    val subStarList = ReviewUtil.calculateStarRating(review.rating, StarType.SUB)

    // 리스트 형식 (싱글 리뷰)
    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("detail-photographer-single-review/${review.reviewId}/0")
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = review.profileImageUri),
                contentDescription = "유저 프로필",
                modifier = Modifier
                    .size(37.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Black, CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Text(text = review.nickname)
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    subStarList.forEach { star ->
                        Image(
                            painter = painterResource(id = star),
                            contentDescription = "별점",
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonIconButton(
                    label = "신고",
                    backgroundColor = MainThemeColor.Gray1,
                    textColor = MainThemeColor.Black,
                    horizontalPadding = 5.dp,
                    verticalPadding = 2.dp,
                    borderRadius = 5.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = review.createdAt)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 리뷰 이미지들
        if (review.photoReviews.isNotEmpty()) {
            when (type) {
                SingleReviewType.OVERVIEW -> {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState()) // horizontalScroll 추가
                            .padding(start = 0.dp, end = 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        review.photoReviews.forEach { review ->
                            Image(
                                painter = rememberAsyncImagePainter(model = review.photoReviewUri),
                                contentDescription = "리뷰 사진",
                                modifier = Modifier.size(114.dp),
                                contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                            )
                        }
                    }
                }

                SingleReviewType.DETAIL -> {
                    val painter =
                        rememberAsyncImagePainter(model = review.photoReviews[photoIndex].photoReviewUri)

                    val imageState = painter.state
                    var aspectRatio by remember { mutableStateOf(1f) }

                    // 이미지의 원본 비율을 계산하여 동적으로 설정
                    LaunchedEffect(imageState) {
                        if (imageState is AsyncImagePainter.State.Success) {
                            val width = imageState.result.drawable.intrinsicWidth
                            val height = imageState.result.drawable.intrinsicHeight
                            if (width > 0 && height > 0) {
                                aspectRatio = width.toFloat() / height.toFloat()
                            }
                        }
                    }

                    Image(
                        painter = painter,
                        contentDescription = "review-image",
                        modifier = Modifier
                            .fillMaxWidth() // 가로를 꽉 채우되
                            .aspectRatio(aspectRatio) // 비율에 맞게 세로 크기 자동 조정
                            .padding(vertical = 0.dp), // 수직 여백 제거
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(
                    text = "옵션", style = pretendardTypography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold, color = MainThemeColor.Gray4
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = review.option,
                    style = pretendardTypography.bodySmall.copy(color = MainThemeColor.Gray4)
                )

            }
            Spacer(modifier = Modifier.height(3.dp))
            Row {
                Text(
                    text = "촬영지", style = pretendardTypography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold, color = MainThemeColor.Gray4
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = review.location,
                    style = pretendardTypography.bodySmall.copy(color = MainThemeColor.Gray4)
                )
            }

            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = review.reviewText, style = pretendardTypography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.5.dp)
            ) {
                Image(painter = painterResource(
                    id = if (review.isRecommended) R.drawable.like_active else R.drawable.like_inactive
                ), contentDescription = "like-inactive", modifier = Modifier.clickable { })
                Text(
                    text = review.recommendationCount.toString(),
                    style = pretendardTypography.bodyMedium.copy(color = MainThemeColor.Gray6)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (type == SingleReviewType.OVERVIEW) {
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SingleReviewPreview() {
    val navController = rememberNavController()
    val dummyReview = PhotographerReview(
        reviewId = 1,
        profileImageUri = "https://picsum.photos/200/300",
        nickname = "사용자1",
        rating = 4.0f,
        createdAt = "2025-02-26",
        isReported = true,
        photoReviews = dummyPhotoReviews.slice(0..2),
        photoReviewCount = 3,
        option = "프로필 Only",
        location = "서울 강남",
        reviewText = "하나하나 신경써서 해주시고 잘 알려주세요 사진 처음찍거나 잘 못찍으시는 분들 하시면 후회 안하십니다!",
        isRecommended = true,
        recommendationCount = 3
    )

    PicplzTheme {
        SingleReview(navController, dummyReview)
    }
}
