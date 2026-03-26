package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.detail_photographer.review.FullScreenPhotoView
import com.hm.picplz.ui.screen.detail_photographer.review.PhotoCarousel
import com.hm.picplz.ui.screen.detail_photographer.review.PhotographerInfoSection
import com.hm.picplz.ui.screen.detail_photographer.review.ReportBottomSheet
import com.hm.picplz.ui.screen.detail_photographer.review.ReviewHeader
import com.hm.picplz.ui.screen.detail_photographer.review.ReviewThumbnailBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailPhotographerSingleReviewScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavController,
    reviewId: Int,
    photoIndex: Int = 0,
) {
    val state by viewModel.state.collectAsState()
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val currentIndex = state.currentReviewIndex
    val review = state.reviews.getOrNull(currentIndex) ?: return

    // 모든 리뷰의 대표 사진 (첫 번째 사진)
    val allThumbnails =
        remember(state.reviews) {
            state.reviews.mapNotNull { r ->
                r.photoReviews.firstOrNull()?.photoReviewUri
            }
        }

    // 사진이 있는 리뷰만의 인덱스 매핑
    val reviewsWithPhotos =
        remember(state.reviews) {
            state.reviews.mapIndexedNotNull { index, r ->
                if (r.photoReviews.isNotEmpty()) index else null
            }
        }

    val thumbnailIndex =
        remember(currentIndex, reviewsWithPhotos) {
            reviewsWithPhotos.indexOf(currentIndex).coerceAtLeast(0)
        }

    val photoCount = review.photoReviews.size.coerceAtLeast(1)
    val initialPage =
        if (currentIndex == reviewId) photoIndex else 0
    val pagerState =
        key(currentIndex) {
            rememberPagerState(
                initialPage = initialPage.coerceIn(0, photoCount - 1),
                pageCount = { photoCount },
            )
        }

    // 초기 진입 시 reviewId를 State에 반영
    LaunchedEffect(Unit) {
        viewModel.handleIntent(DetailPhotographerIntent.SwitchReview(reviewId))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MainThemeColor.White,
            bottomBar = {
                if (allThumbnails.size > 1) {
                    ReviewThumbnailBar(
                        thumbnails = allThumbnails,
                        selectedIndex = thumbnailIndex,
                        onSelect = { index ->
                            if (index < reviewsWithPhotos.size) {
                                viewModel.handleIntent(
                                    DetailPhotographerIntent.SwitchReview(
                                        reviewsWithPhotos[index],
                                    ),
                                )
                            }
                        },
                        onCenterChanged = { index ->
                            if (index < reviewsWithPhotos.size) {
                                viewModel.handleIntent(
                                    DetailPhotographerIntent.SwitchReview(
                                        reviewsWithPhotos[index],
                                    ),
                                )
                            }
                        },
                    )
                }
            },
            content = { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                ) {
                    CommonFixedTopBar(title = "") {
                        viewModel.handleIntent(
                            DetailPhotographerIntent.NavigateToPrev,
                        )
                    }

                    ReviewHeader(
                        modifier = paddingModifier.padding(vertical = 12.dp),
                        review = review,
                        onReport = {
                            viewModel.handleIntent(
                                DetailPhotographerIntent.ToggleReportSheet,
                            )
                        },
                    )

                    if (review.photoReviews.isNotEmpty()) {
                        PhotoCarousel(
                            photos = review.photoReviews.map { it.photoReviewUri },
                            pagerState = pagerState,
                            onPhotoClick = { uri ->
                                viewModel.handleIntent(
                                    DetailPhotographerIntent.ShowFullScreenPhoto(
                                        uri,
                                    ),
                                )
                            },
                        )
                    }

                    PhotographerInfoSection(
                        modifier = paddingModifier,
                        profileImageUri = state.profileInfo.profileImageUri,
                        photographerName = state.profileInfo.name,
                        photographerId = state.profileInfo.id,
                        review = review,
                        navController = navController,
                    )

                    HorizontalDivider(
                        color = MainThemeColor.Gray2,
                        thickness = 1.dp,
                        modifier = paddingModifier.fillMaxWidth(),
                    )

                    Text(
                        text = review.reviewText,
                        style = MainThemeFont.Body,
                        color = MainThemeColor.Black,
                        modifier =
                            paddingModifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                    )
                }
            },
        )

        state.fullScreenImageUri?.let { uri ->
            FullScreenPhotoView(
                imageUri = uri,
                onDismiss = {
                    viewModel.handleIntent(
                        DetailPhotographerIntent.DismissFullScreenPhoto,
                    )
                },
            )
        }
    }

    ReportBottomSheet(
        visible = state.isReportSheetVisible,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleReportSheet)
        },
        onSelect = { /* TODO: 신고 API 연동 */ },
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
