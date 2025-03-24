package com.hm.picplz.ui.screen.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.absoluteValue

enum class DateComponent(val unit: String) {
    YEAR("년"),
    MONTH("월"),
    DAY("일");

    fun labelOf(value: Int): String = "$value$unit"
}

private val PickerItemHeight = 40.dp
private val PickerHeight = 200.dp
private const val FontSizeMax = 18f
private const val FontSizeMin = 14f

/**
 * CommonDateScrollPicker
 * 년, 월, 일을 선택할 수 있는 스크롤 피커
 *
 * @param selectedDate [LocalDate] : 선택된 날짜 정보
 * @param onDateSelected [(LocalDate) -> Unit] : picker를 통해 년/월/일을 변경했을 때 호출되는 콜백함수
 * */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommonDateScrollPicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val currentDate = LocalDate.now()

    val yearList = (1980..currentDate.year).toList() // 현재 년도까지만 노출
    val monthList = (1..12).toList()

    // 선택된 날짜를 기준으로 초기 스크롤 위치 설정
    val yearListState = rememberLazyListState(selectedDate.year - 1980)
    val monthListState = rememberLazyListState(selectedDate.monthValue - 1)
    val dayListState = rememberLazyListState(selectedDate.dayOfMonth - 1)

    val density = LocalDensity.current
    val thresholdPx = remember { density.run { (PickerItemHeight / 2).toPx() } }

    // 현재 스크롤 상태를 기준으로 선택된 날짜 계산
    val year by remember { derivedStateOf { (yearListState.firstVisibleItemIndex + if (yearListState.firstVisibleItemScrollOffset >= thresholdPx) 1981 else 1980) } }
    val month by remember { derivedStateOf { (monthListState.firstVisibleItemIndex + if (monthListState.firstVisibleItemScrollOffset >= thresholdPx) 2 else 1) } }

    val maxDay = YearMonth.of(year, month).lengthOfMonth()

    val day by remember {
        derivedStateOf {
            (dayListState.firstVisibleItemIndex + if (dayListState.firstVisibleItemScrollOffset >= thresholdPx) 2 else 1)
                .coerceAtMost(maxDay) // 해당 월의 최대 날짜를 초과하지 않도록 제한
        }
    }

    LaunchedEffect(year, month, day) {
        onDateSelected(LocalDate.of(year, month, day))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 선택된 항목 강조용 중앙 하이라이트 박스
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PickerItemHeight)
                .background(Color(0xFFE6E7EB), RoundedCornerShape(8.dp))
                .zIndex(0f)
        )

        // 년, 월, 일 Picker 각각의 스크롤 컬럼 구성
        Row(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(PickerHeight)
                .align(Alignment.Center)
        ) {
            CommonPicker(
                items = yearList,
                listState = yearListState,
                dateType = DateComponent.YEAR,
                modifier = Modifier.weight(1f),
            )
            CommonPicker(
                items = monthList,
                listState = monthListState,
                dateType = DateComponent.MONTH,
                modifier = Modifier.weight(1f),
            )
            CommonPicker(
                items = (1..maxDay).toList(),
                listState = dayListState,
                dateType = DateComponent.DAY,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonPicker(
    items: List<Int>,
    listState: LazyListState,
    dateType: DateComponent,
    modifier: Modifier = Modifier,
) {
    val itemHeightPx = with(LocalDensity.current) { PickerItemHeight.toPx() }
    val scrollOffsetPx by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex * itemHeightPx + listState.firstVisibleItemScrollOffset
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 80.dp),
        flingBehavior = rememberSnapFlingBehavior(listState),
        modifier = modifier.height(PickerHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items) { index, num ->
            val currentItemOffset = index * itemHeightPx
            val distanceFromCenterPx = (currentItemOffset - scrollOffsetPx).absoluteValue
            val distanceRatio = distanceFromCenterPx / itemHeightPx

            val alpha = (1f - distanceRatio * 0.55f).coerceIn(0.2f, 1f)
            val fontSize =
                (FontSizeMax - distanceRatio * 2f).coerceAtLeast(FontSizeMin).sp

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PickerItemHeight)
            ) {
                Text(
                    text = dateType.labelOf(num),
                    fontSize = fontSize,
                    color = MainThemeColor.Black.copy(alpha)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CommonDateScrollPickerPreview() {
    val selectedDate = LocalDate.now()

    PicplzTheme {
        CommonDateScrollPicker(
            selectedDate,
        )
    }
}