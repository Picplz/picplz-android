package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun PhotoCarousel(
    photos: List<String>,
    pagerState: PagerState,
    onPhotoClick: (String) -> Unit,
) {
    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(model = photos[page]),
                contentDescription = stringResource(PhotographerR.string.review_photo),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { onPhotoClick(photos[page]) },
                contentScale = ContentScale.Crop,
            )
        }

        // dots 영역 고정 높이 — 1장이어도 레이아웃 시프트 방지
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(DOT_AREA_HEIGHT),
            contentAlignment = Alignment.Center,
        ) {
            if (photos.size > 1) {
                PageIndicatorDots(
                    totalCount = photos.size,
                    currentPage = pagerState.currentPage,
                )
            }
        }
    }
}

/**
 * 인스타그램 스타일 슬라이딩 dots indicator.
 * - 최대 5개 표시, 선택된 dot은 가운데 유지
 * - 가운데서 멀수록 작아짐: 6 → 4 → 2
 * - 처음/마지막 2개에서는 edge에 고정
 */
@Composable
fun PageIndicatorDots(
    totalCount: Int,
    currentPage: Int,
) {
    if (totalCount <= 0) return

    val visibleCount = minOf(totalCount, MAX_VISIBLE_DOTS)
    // 슬라이딩 윈도우 시작 인덱스
    val windowStart =
        when {
            totalCount <= MAX_VISIBLE_DOTS -> 0
            currentPage <= CENTER_INDEX -> 0
            currentPage >= totalCount - CENTER_INDEX - 1 -> totalCount - MAX_VISIBLE_DOTS
            else -> currentPage - CENTER_INDEX
        }
    // 윈도우 안에서 선택된 dot의 위치
    val selectedPosInWindow =
        when {
            totalCount <= MAX_VISIBLE_DOTS -> currentPage
            currentPage <= CENTER_INDEX -> currentPage
            currentPage >= totalCount - CENTER_INDEX - 1 ->
                MAX_VISIBLE_DOTS - (totalCount - currentPage)
            else -> CENTER_INDEX
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 0 until visibleCount) {
            val pageIndex = windowStart + i
            val distFromSelected =
                kotlin.math.abs(i - selectedPosInWindow)
            val dotSize =
                when (distFromSelected) {
                    0 -> DOT_LARGE
                    1 -> DOT_MEDIUM
                    else -> DOT_SMALL
                }
            val dotColor =
                if (pageIndex == currentPage) {
                    MainThemeColor.Black
                } else {
                    MainThemeColor.Black.copy(alpha = 0.2f)
                }

            if (i > 0) Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier =
                    Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(dotColor),
            )
        }
    }
}

private val DOT_AREA_HEIGHT = 18.dp
private const val MAX_VISIBLE_DOTS = 5
private const val CENTER_INDEX = 2
private val DOT_LARGE = 6.dp
private val DOT_MEDIUM = 4.dp
private val DOT_SMALL = 2.dp
