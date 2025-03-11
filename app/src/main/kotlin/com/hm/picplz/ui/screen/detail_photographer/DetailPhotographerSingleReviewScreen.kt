package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.data.model.PhotoReview
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.detail_photographer.Review.SingleReview
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.utils.SingleReviewType
import com.hm.picplz.viewmodel.DetailPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DetailPhotographerSingleReviewScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavController,
    reviewId: Int,
    photoIndex: Int = 0,
) {
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    val currentState = viewModel.state.collectAsState().value
    val singleReview = currentState.reviews[reviewId]

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() } // Dp -> Px 변환

    // 값들 추출
    val imageWidth = 40.dp
    val selectedImageWidth = 50.dp
    val imageSpacing = 1.dp

    val imageWidthPx = with(LocalDensity.current) { imageWidth.toPx() }
    val selectedImageWidthPx = with(LocalDensity.current) { selectedImageWidth.toPx() }
    val imageSpacingPx = with(LocalDensity.current) { imageSpacing.toPx() }
    val horizontalPaddingPx = with(LocalDensity.current) { (screenWidthDp / 2).toPx() }

    val scrollState = rememberScrollState()
    var currentPhotoIndex by remember { mutableStateOf(photoIndex) }

    val coroutineScope = rememberCoroutineScope()

    // 스크롤 위치 계산 함수 개선
    fun calculateScrollOffset(index: Int): Int {
        val offsetFromStart = index * (imageWidthPx + imageSpacingPx)
        val adjustedOffsetFromStart = if (index == currentPhotoIndex) {
            offsetFromStart - (selectedImageWidthPx - imageWidthPx) / 2
        } else {
            offsetFromStart
        }

        // 더 정확한 계산 방식 적용
        return (adjustedOffsetFromStart - (screenWidthPx / 2) + (imageWidthPx / 2) + horizontalPaddingPx).toInt()
    }

    // 화면 로드 시 초기 scroll position 계산
    LaunchedEffect(photoIndex) {
        val offsetFromCenter = calculateScrollOffset(photoIndex)
        scrollState.animateScrollTo(offsetFromCenter)
    }

    // 스크롤 중앙 이미지 인덱스 업데이트
    LaunchedEffect(scrollState.value) {
        val centerPosition = scrollState.value + (screenWidthPx / 2)
        val newPhotoIndex =
            ((centerPosition - horizontalPaddingPx) / (imageWidthPx + imageSpacingPx)).toInt()

        if (newPhotoIndex in 0 until singleReview.photoReviewCount && newPhotoIndex != currentPhotoIndex) {
            currentPhotoIndex = newPhotoIndex
        }
    }

    // UI 구성
    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Main content area (scrollable)
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .background(MainThemeColor.White)
                            .zIndex(1f)
                            .height(56.dp)
                    ) {
                        CommonTopBar(text = "${singleReview.nickname} ${currentPhotoIndex + 1} / ${singleReview.photoReviewCount}",
                            onClickBack = { viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev) })
                    }

                    Column(modifier = paddingModifier.fillMaxSize()) {
                        SingleReview(
                            review = singleReview,
                            type = SingleReviewType.DETAIL,
                            photoIndex = currentPhotoIndex
                        )

                        Spacer(modifier = Modifier.height(80.dp)) // Ensure there's space at the bottom
                    }
                }

                // Floating PhotoScroller at the bottom, with zIndex to stay on top
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .zIndex(1f)
                        .height(80.dp)
                        .background(MainThemeColor.White),
                ) {
                    Box(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        PhotoScroller(
                            scrollState = scrollState,
                            photoIndex = currentPhotoIndex,
                            images = singleReview.photoReviews,
//                            calculateScrollOffset = ::calculateScrollOffset,
                            onImageClick = { index ->
                                if (index != currentPhotoIndex) {
                                    // 클릭 시 currentPhotoIndex를 바로 업데이트하고 스크롤 애니메이션 적용
                                    currentPhotoIndex = index
                                    coroutineScope.launch {
                                        val offset = calculateScrollOffset(index)
                                        scrollState.animateScrollTo(offset)
                                    }
                                }
                            }
                        )
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


// 이미지 스크롤러 컴포저블
@Composable
fun PhotoScroller(
    scrollState: ScrollState,
    photoIndex: Int,
    images: List<PhotoReview>,
    onImageClick: (Int) -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val imageWidth = 40.dp
    val selectedImageWidth = 50.dp
    val imageSpacing = 1.dp

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = screenWidthDp / 2),
        horizontalArrangement = Arrangement.spacedBy(imageSpacing),
        verticalAlignment = Alignment.Bottom
    ) {
        images.forEachIndexed { index, image ->
            val isSelected = index == photoIndex
            val animatedSize by animateDpAsState(
                targetValue = if (isSelected) selectedImageWidth else imageWidth,
            )

            if (isSelected) Spacer(modifier = Modifier.width(10.dp))

            Image(
                painter = rememberAsyncImagePainter(model = image.photoReviewUri),
                contentDescription = "review-image",
                modifier = Modifier
                    .size(animatedSize)
                    .clickable {
                        onImageClick(index)
                    },
                contentScale = ContentScale.Crop
            )

            if (isSelected) Spacer(modifier = Modifier.width(10.dp))
        }
    }
}
