package com.hm.picplz.ui.screen.detail_photographer.review

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.feature.photographer.R as PhotographerR

/**
 * 하단 고정 썸네일 바.
 * 모든 리뷰의 대표 사진을 나열하고, 선택된 리뷰가 가운데에 위치.
 */
@Composable
fun ReviewThumbnailBar(
    thumbnails: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    onCenterChanged: (Int) -> Unit,
) {
    val listState = rememberLazyListState()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val centerPadding = (screenWidthDp - THUMBNAIL_SIZE) / 2
    var isUserDragging by remember { mutableStateOf(false) }

    LaunchedEffect(listState.interactionSource) {
        listState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    isUserDragging = true
                }
                is DragInteraction.Stop,
                is DragInteraction.Cancel,
                -> {
                    isUserDragging = false
                    val centerIndex = listState.findCenterItemIndex()
                    if (centerIndex != -1 && centerIndex != selectedIndex) {
                        onCenterChanged(centerIndex)
                    }
                }
            }
        }
    }

    LaunchedEffect(selectedIndex) {
        if (!isUserDragging) {
            // 약간 지연 후 센터링 — 애니메이션 시작 후 size에 gap 포함되도록
            kotlinx.coroutines.delay(CENTERING_DELAY_MS)
            listState.centerItem(selectedIndex)
        }
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        contentPadding = PaddingValues(horizontal = centerPadding),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        itemsIndexed(thumbnails) { index, uri ->
            val isSelected = index == selectedIndex
            val animatedSize by animateDpAsState(
                targetValue = if (isSelected) THUMBNAIL_SELECTED_SIZE else THUMBNAIL_SIZE,
                animationSpec = tween(durationMillis = 250),
                label = "thumbnailSize",
            )
            val animatedGap by animateDpAsState(
                targetValue = if (isSelected) SELECTED_GAP else 0.dp,
                animationSpec = tween(durationMillis = 250),
                label = "thumbnailGap",
            )
            Spacer(modifier = Modifier.width(animatedGap))
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = stringResource(PhotographerR.string.review_photo),
                modifier =
                    Modifier
                        .size(animatedSize)
                        .clickable { onSelect(index) },
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(animatedGap))
        }
    }
}

private suspend fun androidx.compose.foundation.lazy.LazyListState.centerItem(index: Int) {
    val layoutInfo = layoutInfo
    val target = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

    if (target != null) {
        val containerSize =
            layoutInfo.viewportSize.width -
                layoutInfo.beforeContentPadding -
                layoutInfo.afterContentPadding
        val targetCenter = target.offset + target.size / 2f
        val viewportCenter = containerSize / 2f
        animateScrollBy(targetCenter - viewportCenter)
    } else {
        scrollToItem(index)
        val retarget =
            layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
        if (retarget != null) {
            val containerSize =
                layoutInfo.viewportSize.width -
                    layoutInfo.beforeContentPadding -
                    layoutInfo.afterContentPadding
            val targetCenter = retarget.offset + retarget.size / 2f
            val viewportCenter = containerSize / 2f
            animateScrollBy(targetCenter - viewportCenter)
        }
    }
}

internal fun androidx.compose.foundation.lazy.LazyListState.findCenterItemIndex(): Int {
    val layoutInfo = layoutInfo
    val containerSize =
        layoutInfo.viewportSize.width -
            layoutInfo.beforeContentPadding -
            layoutInfo.afterContentPadding
    val viewportCenter = containerSize / 2f

    return layoutInfo.visibleItemsInfo.minByOrNull { item ->
        val itemCenter = item.offset + item.size / 2f
        kotlin.math.abs(itemCenter - viewportCenter)
    }?.index ?: -1
}

private const val CENTERING_DELAY_MS = 50L
private val THUMBNAIL_SIZE = 40.dp
private val THUMBNAIL_SELECTED_SIZE = 50.dp
private val SELECTED_GAP = 20.dp
