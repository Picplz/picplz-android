package com.hm.picplz.ui.screen.detail_photographer

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.DetailPhotographerPhotoReviews
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.detail_photographer.review.ReportBottomSheet
import com.hm.picplz.ui.screen.detail_photographer.review.ReviewSortBottomSheet
import com.hm.picplz.ui.screen.detail_photographer.review.SingleReview
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.ReviewUtil
import com.hm.picplz.ui.util.StarType
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun DetailPhotographerReviewScreen(
    navController: NavController,
    photographerId: Int,
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val reviewSummary = state.reviewSummary
    val reviews = state.reviews
    val totalRating = reviewSummary.averageRating
    val starList = ReviewUtil.calculateStarRating(totalRating, StarType.MAIN)
    val images = reviewSummary.photoReviews

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
            ) {
                CommonFixedTopBar(
                    title = stringResource(PhotographerR.string.review_title),
                ) {
                    viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                }

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    Column(
                        modifier = paddingModifier.padding(top = 20.dp),
                    ) {
                        // 촬영 만족도 + 별
                        Text(
                            text =
                                stringResource(
                                    PhotographerR.string.shooting_satisfaction,
                                ),
                            style = MainThemeFont.TitleSmall,
                            color = MainThemeColor.Black,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            starList.forEach { star ->
                                Image(
                                    painter = painterResource(id = star),
                                    contentDescription =
                                        stringResource(
                                            PhotographerR.string.star_rating,
                                        ),
                                    modifier = Modifier.height(20.dp),
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = totalRating.toString(),
                                style = MainThemeFont.BodyBold,
                                color = MainThemeColor.Gray4,
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 리뷰 N
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(PhotographerR.string.review_title),
                                style = MainThemeFont.ButtonDefault,
                                color = MainThemeColor.Black,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = reviewSummary.totalReviewCount.toString(),
                                style = MainThemeFont.Body,
                                color = MainThemeColor.Gray3,
                            )
                        }

                        Spacer(modifier = Modifier.height(9.dp))

                        // 리뷰 사진 그리드
                        ReviewPhotoGrid(
                            images = images.map { it.photoReviewUri },
                            onShowAll = {
                                navController.navigate(
                                    DetailPhotographerPhotoReviews(photographerId),
                                )
                            },
                        )
                    }

                    HorizontalDivider(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                        thickness = 10.dp,
                        color = MainThemeColor.Gray1,
                    )

                    Column(modifier = paddingModifier) {
                        // 정렬 버튼
                        CommonIconButton(
                            label = state.reviewSortType.label,
                            backgroundColor = MainThemeColor.Transparent,
                            textColor = MainThemeColor.Gray5,
                            textStyle = MainThemeFont.Caption,
                            iconResId = R.drawable.arrow_down,
                            iconSize = 12.dp,
                            location = "right",
                            horizontalPadding = 0.dp,
                            verticalPadding = 0.dp,
                            gap = 2.dp,
                            onClick = {
                                viewModel.handleIntent(
                                    DetailPhotographerIntent.ToggleSortSheet,
                                )
                            },
                            modifier =
                                Modifier.padding(top = 20.dp, bottom = 8.dp),
                        )

                        // 리뷰 리스트
                        reviews.forEach { item ->
                            SingleReview(
                                navController = navController,
                                review = item,
                                onReport = {
                                    viewModel.handleIntent(
                                        DetailPhotographerIntent.ToggleReportSheet,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        },
    )

    // 신고 바텀시트
    ReportBottomSheet(
        visible = state.isReportSheetVisible,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleReportSheet)
        },
        onSelect = { /* TODO: 신고 API 연동 */ },
    )

    // 정렬 바텀시트
    ReviewSortBottomSheet(
        visible = state.isSortSheetVisible,
        selectedSortType = state.reviewSortType,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleSortSheet)
        },
        onSelect = { sortType ->
            viewModel.handleIntent(
                DetailPhotographerIntent.SelectReviewSort(sortType),
            )
        },
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is DetailPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun ReviewPhotoGrid(
    images: List<String>,
    onShowAll: () -> Unit,
) {
    if (images.isEmpty()) return

    val hasOverflow = images.size > 4

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        images.take(minOf(3, images.size)).forEach { imageUri ->
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = stringResource(PhotographerR.string.review_photo),
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
        }

        if (hasOverflow) {
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable { onShowAll() },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = images[3]),
                    contentDescription = stringResource(PhotographerR.string.review_photo),
                    modifier =
                        Modifier
                            .matchParentSize()
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    color = MainThemeColor.Black.copy(alpha = 0.5f),
                                    size = size,
                                )
                            },
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = "+${images.size - 4}",
                    color = MainThemeColor.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }
        } else if (images.size == 4) {
            Image(
                painter = rememberAsyncImagePainter(model = images[3]),
                contentDescription = stringResource(PhotographerR.string.review_photo),
                modifier =
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerReviewScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerReviewScreen(
            navController = navController,
            photographerId = 1,
        )
    }
}
