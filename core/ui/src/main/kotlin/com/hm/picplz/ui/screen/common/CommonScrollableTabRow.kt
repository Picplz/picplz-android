package com.hm.picplz.ui.screen.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun CommonScrollableTabRow(
    modifier: Modifier = Modifier,
    isIndicatorMatchTextWidth: Boolean = false,
    textStyle: TextStyle = MainFontFamily.bodyLarge,
    selectedTabIndex: Int,
    tabTitles: List<String>,
    onTabSelected: (Int) -> Unit,
    indicatorHeight: Dp = 2.dp,
    containerColor: Color = MainThemeColor.White,
    contentColor: Color = MainThemeColor.Black,
    selectedTextColor: Color = MainThemeColor.Black,
    unselectedTextColor: Color = MainThemeColor.Gray,
    indicatorColor: Color = MainThemeColor.Black,
    dividerColor: Color = MainThemeColor.Gray2
) {
    val density = LocalDensity.current
    val tabWidths = remember {
        mutableStateListOf<Dp>().apply {
            repeat(tabTitles.size) { add(0.dp) }
        }
    }

    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = 0.dp,
        divider = {
            HorizontalDivider(thickness = 1.dp, color = dividerColor)
        },
        indicator = { tabPositions ->
            val indicatorModifier = if (isIndicatorMatchTextWidth) {
                Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[selectedTabIndex],
                    tabWidth = tabWidths[selectedTabIndex]
                )
            } else {
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
            }

            TabRowDefaults.SecondaryIndicator(
                modifier = indicatorModifier,
                color = indicatorColor,
                height = indicatorHeight
            )
        }
    ) {
        tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (isSelected) selectedTextColor else unselectedTextColor,
                        onTextLayout = {
                            tabWidths[index] = with(density) { it.size.width.toDp() }
                        },
                        style = textStyle
                    )
                }
            )
        }
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}