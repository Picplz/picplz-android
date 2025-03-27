package com.hm.picplz.ui.screen.detail_photographer

import CommonChip
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.navigation.navigateWithBundle
import com.hm.picplz.ui.screen.common.CommonDropdownMenu
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.common.DropdownMenuItemData
import com.hm.picplz.ui.screen.detail_photographer.review.ReviewBars
import com.hm.picplz.ui.screen.detail_photographer.review.SingleReview
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainTypography
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.button
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.utils.ReviewUtil
import com.hm.picplz.utils.StarType
import com.hm.picplz.viewmodel.DetailPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPhotographerReviewScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentState = viewModel.state.collectAsState().value
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val reviewSummary = currentState.reviewSummary
    val reviews = currentState.reviews

    val totalRating = reviewSummary.averageRating
    val starList = ReviewUtil.calculateStarRating(totalRating, StarType.MAIN)

    val images = reviewSummary.photoReviews
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("추천순") }

    val reviewBarItems = reviewSummary.keywordBars

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                CommonFixedTopBar(title = "리뷰") {
                    viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                }

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()) // verticalScroll을 적용
                ) {
                    Column(modifier = paddingModifier) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "촬영 만족도", style = MainTypography.button)
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

                        ReviewBars(items = reviewBarItems)

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "리뷰", style = MainTypography.button)
                            Spacer(modifier = Modifier.width(4.dp)) // 텍스트 사이에 간격 추가
                            Text(text = reviewSummary.totalReviewCount.toString())
                        }

                        Spacer(modifier = Modifier.height(9.dp))

                        // 리뷰 사진 모아보기
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            images.take(3).forEach { image ->
                                Image(
                                    painter = rememberAsyncImagePainter(model = image.photoReviewUri),
                                    contentDescription = "리뷰 사진",
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f), // 원하는 크기로 고정
                                    contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                )
                            }

                            // 4개 이상일 경우 마지막 사진에 원본 이미지 + 어두운 오버레이 + "+N" 표시
                            if (images.size > 4) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clickable {
                                            // Bundle에 데이터 넣기
                                            val bundle = Bundle().apply {
                                                putParcelableArrayList(
                                                    "photo-reviews",
                                                    ArrayList((images))
                                                )
                                            }

                                            // Bundle을 navigate의 두 번째 인자로 전달
                                            navController.navigateWithBundle(
                                                "detail-photographer-photo-reviews",
                                                bundle
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    // 원본 이미지
                                    Image(
                                        painter = rememberAsyncImagePainter(model = images[3].photoReviewUri), // 네 번째 이미지를 기준으로
                                        contentDescription = "리뷰 사진",
                                        modifier = Modifier
                                            .matchParentSize()
                                            .drawWithContent {
                                                drawContent()
                                                drawRect(
                                                    color = MainThemeColor.Black.copy(alpha = 0.5f), // 어두운 오버레이 효과
                                                    size = size
                                                )
                                            },
                                        contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                    )

                                    // "+N" 텍스트 오버레이
                                    Text(
                                        text = "+${images.size - 4}", // 초과된 개수 표시
                                        color = MainThemeColor.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                            } else if (images.size == 4) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = images[3]),
                                    contentDescription = "리뷰 사진",
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentScale = ContentScale.Crop // 이미지 중앙을 기준으로 크기를 맞추고 자름
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        thickness = 10.dp,
                        color = MainThemeColor.Gray2
                    )

                    Column(modifier = paddingModifier) {
                        // chip 필터링
                        // TODO: 디자인 적용
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            CommonChip(label = "프로필 Only", isSelected = true, isEditable = false)
                            CommonChip(label = "카카오톡 패키지", isSelected = false, isEditable = false)
                            CommonChip(label = "인스타그램 패키지", isSelected = false, isEditable = false)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 추천순 / 최신순
                        CommonDropdownMenu(
                            initialSelectedText = "최신순",
                            triggerButton = { label ->
                                CommonIconButton(
                                    label = label,
                                    backgroundColor = MainThemeColor.Transparent,
                                    textColor = MainThemeColor.Gray5,
                                    textStyle = pretendardTypography.bodySmall,
                                    iconResId = R.drawable.arrow_down,
                                    location = "right",
                                    horizontalPadding = 0.dp,
                                    verticalPadding = 0.dp,
                                    gap = 4.dp,
                                    borderRadius = 10.dp
                                )
                            },
                            menuItems = listOf(
                                DropdownMenuItemData("추천순", MainThemeColor.Gray5, itemOnClick = {}),
                                DropdownMenuItemData("최신순", MainThemeColor.Gray5),
                            )
                        )

                        // 리스트 형식 (싱글 리뷰)
                        reviews.forEach { item ->
                            SingleReview(navController, item)
                        }
                    }
                }
            }
        })

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is DetailPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerReviewScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerReviewScreen(navController = navController)
    }
}
