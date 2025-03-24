package com.hm.picplz.ui.screen.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.launch

object PagerConfig {
    const val BASE_DURATION = 200 // 페이지 전환 기본 시간
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CommonHorizontalPager(
    modifier: Modifier = Modifier,
    items: List<T>, // 표시할 아이템 리스트
    pageCount: Int, // 전체 아이템 개수
    initialPage: Int = 0, // 처음 표시할 인덱스
    showIndicator: Boolean = false,  // Indicator 표시 여부
    isIndicatorPositionAbsolute: Boolean = false, // Indicator의 위치를 절대적으로 할 것인지, 상대적으로 할 것인지
    /*
    * Indicator 상단 여백을 정의하는 값(dp)
    * - "상대적 위치"일 경우: HorizontalPager 아래에 위채, 해당 값을 기준으로 상단 여백이 적용됨
    * - "절대적 위치"일 경우: 화면 상단에서부터의 여백을 기준으로 Indicator가 고정 위치로 표시됨
    * */
    indicatorTopSpacing: Dp = 10.dp,
    itemContent: @Composable (T) -> Unit, // 각 페이지의 UI를 구성하는 Composable 함수
) {
    val pagerState = rememberPagerState(pageCount = { pageCount }, initialPage = initialPage)

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = pageCount > 1 // 아이템이 한개 이상일 때만 스크롤 활성화
        ) { page ->
            itemContent(items[page])
        }

        if (showIndicator && !isIndicatorPositionAbsolute) {
            CommonHorizontalPagerWithRelativeIndicator(
                pagerState = pagerState,
                indicatorTopSpacing = indicatorTopSpacing
            )
        }
    }

    if (showIndicator && isIndicatorPositionAbsolute) {
        CommonHorizontalPagerWithAbsoluteIndicator(
            pagerState = pagerState,
            indicatorTopSpacing = indicatorTopSpacing
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonHorizontalPagerWithRelativeIndicator(
    pagerState: PagerState,
    indicatorTopSpacing: Dp,
) {
    Spacer(modifier = Modifier.height(indicatorTopSpacing))
    CommonHorizontalPagerIndicator(pagerState = pagerState)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonHorizontalPagerWithAbsoluteIndicator(
    pagerState: PagerState,
    indicatorTopSpacing: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .padding(top = indicatorTopSpacing)
    ) {
        CommonHorizontalPagerIndicator(pagerState = pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonHorizontalPagerIndicator(
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    val pageCount = pagerState.pageCount
    val currentPage = pagerState.currentPage

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        repeat(pageCount) { iteration ->
            val isSelected = currentPage == iteration
            val indicatorColor = if (isSelected) MainThemeColor.Black else MainThemeColor.White

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(indicatorColor)
                    .border(1.dp, MainThemeColor.Black, CircleShape)
                    .size(10.dp)
                    .clickable {
                        coroutineScope.launch {
                            animatePageScroll(pagerState, currentPage, iteration, pageCount)
                        }
                    }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private suspend fun animatePageScroll(
    pagerState: PagerState,
    currentPage: Int,
    targetPage: Int,
    pageCount: Int
) {
    // 이동 방향 계산
    val direction = if (currentPage < targetPage) 1 else -1

    // 페이지 이동 범위(오름차순/내림차순)
    val range = if (direction == 1) {
        currentPage until targetPage
    } else {
        currentPage downTo targetPage + 1
    }

    // 페이지를 순차적으로 애니메이션
    for (page in range) {
        pagerState.animateScrollToPage(
            page = (page + direction + pageCount) % pageCount,
            animationSpec = tween(durationMillis = PagerConfig.BASE_DURATION)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CommonHorizontalPagerIndicatorPreview() {
    val pageCount = 10
    val pagerState = rememberPagerState(pageCount = { pageCount })

    PicplzTheme {
        CommonHorizontalPagerIndicator(pagerState)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonHorizontalPagerPreview() {
    // 예시 데이터
    val items = List(5) { "Page $it" }
    val pageCount = items.size
    val initialPage = 0
    val showIndicator = true

    PicplzTheme {
        CommonHorizontalPager(
            items = items,
            pageCount = pageCount,
            initialPage = initialPage,
            showIndicator = showIndicator
        ) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MainThemeColor.Black)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item, color = MainThemeColor.White)
            }
        }
    }
}