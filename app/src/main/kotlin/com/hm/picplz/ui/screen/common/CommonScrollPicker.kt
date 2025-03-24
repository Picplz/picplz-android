package com.hm.picplz.ui.screen.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.hm.picplz.ui.theme.PicplzTheme
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.absoluteValue

private val PickerHeight = 200.dp
private val PickerItemHeight = 40.dp
private const val FontSizeMax = 18f
private const val FontSizeMin = 14f

/**
 * PickerColumnConfig
 *
 * ScrollPicker에서 개별 컬럼을 구성할 때 사용하는 설정 객체입니다.
 *
 * @param items 사용자가 선택할 수 있는 값 목록
 * @param initialIndex 초기 선택 인덱스 (기본값 0)
 * @param labelFormatter 항목을 원하는 포맷의 텍스트로 변환하는 함수. 예: { it -> \"$it개\" }
 */
data class PickerColumnConfig<T>(
    val items: List<T>,
    val initialIndex: Int = 0,
    val labelFormatter: (T) -> String
)

/**
 * CommonScrollPicker
 *
 * 여러 개의 ScrollPickerColumn을 조합하여 숫자, 날짜, 시간 등 다양한 항목을 선택할 수 있는 범용 스크롤 피커
 * 항목 수에 제한이 없으며, 각 항목은 개별적으로 설정된 리스트와 표시 형식을 가질 수 있음
 *
 * @param columns 각 항목은 선택할 값 리스트와 라벨 포맷 함수를 포함
 * @param onValuesSelected 사용자가 값을 선택했을 때 호출되는 콜백. 선택된 항목 리스트가 순서대로 전달
 * @param modifier 외부에서 전달할 수 있는 Modifier. (기본적으로 너비는 가득 채우고, 가운데 정렬됨)
 *
 * @see PickerColumnConfig 항목 구성 데이터 클래스
 * @sample DateScrollPickerPreview 년/월/일 선택 예시
 * @sample TimeScrollPickerPreview 시간 선택 예시
 * @sample SingleNumberScrollPickerPreview 단일 숫자 선택 예시
 */
@Composable
fun <T> CommonScrollPicker(
    columns: List<PickerColumnConfig<T>>,
    onValuesSelected: (List<T>) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemHeightPx = with(LocalDensity.current) { PickerItemHeight.toPx() }

    val listStates = columns.map { config ->
        rememberLazyListState(config.initialIndex)
    }

    val selectedValues by remember {
        derivedStateOf {
            columns.mapIndexed { index, config ->
                val state = listStates[index]
                val offset = if (state.firstVisibleItemScrollOffset >= itemHeightPx / 2) 1 else 0
                config.items.getOrElse(state.firstVisibleItemIndex + offset) {
                    config.items.last()
                }
            }
        }
    }

    LaunchedEffect(selectedValues) {
        onValuesSelected(selectedValues)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F6FA)),
        contentAlignment = Alignment.Center
    ) {
        // 하이라이트 박스 (선택 라인)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PickerItemHeight)
                .background(Color(0xFFE6E7EB))
                .zIndex(0f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(PickerHeight),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            columns.forEachIndexed { index, config ->
                CommonPickerColumn(
                    config = config,
                    state = listStates[index],
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/** 단일 PickerColumn (재사용 가능) */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CommonPickerColumn(
    config: PickerColumnConfig<T>,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    val itemHeightPx = with(LocalDensity.current) { PickerItemHeight.toPx() }
    val scrollOffsetPx by remember {
        derivedStateOf {
            state.firstVisibleItemIndex * itemHeightPx + state.firstVisibleItemScrollOffset
        }
    }

    LazyColumn(
        state = state,
        contentPadding = PaddingValues(vertical = 80.dp),
        flingBehavior = rememberSnapFlingBehavior(state),
        modifier = modifier.height(PickerHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(config.items) { index, item ->
            val currentItemOffset = index * itemHeightPx
            val distanceFromCenterPx =
                (currentItemOffset - scrollOffsetPx).absoluteValue
            val distanceRatio = distanceFromCenterPx / itemHeightPx

            val alpha = (1f - distanceRatio * 0.55f).coerceIn(0.2f, 1f)
            val fontSize = (FontSizeMax - distanceRatio * 2f).coerceAtLeast(FontSizeMin).sp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PickerItemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.labelFormatter(item),
                    fontSize = fontSize,
                    color = Color.Black.copy(alpha = alpha)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateScrollPicker(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit
) {
    val currentYear = LocalDate.now().year
    val yearList = (1980..currentYear).toList()
    val monthList = (1..12).toList()

    // remember: maxDay는 month/year 선택에 따라 유동적이므로 상태로 관리
    var selectedYear by remember { mutableStateOf(initialDate.year) }
    var selectedMonth by remember { mutableStateOf(initialDate.monthValue) }

    val maxDay = remember(selectedYear, selectedMonth) {
        YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
    }

    val dayList = (1..maxDay).toList()

    CommonScrollPicker(
        columns = listOf(
            PickerColumnConfig(
                items = yearList,
                initialIndex = initialDate.year - 1980,
                labelFormatter = { "${it}년" }
            ),
            PickerColumnConfig(
                items = monthList,
                initialIndex = initialDate.monthValue - 1,
                labelFormatter = { "${it}월" }
            ),
            PickerColumnConfig(
                items = dayList,
                initialIndex = initialDate.dayOfMonth - 1,
                labelFormatter = { "${it}일" }
            )
        ),
        onValuesSelected = { values ->
            val year = values[0] as Int
            val month = values[1] as Int
            val day = (values[2] as Int).coerceAtMost(
                YearMonth.of(year, month).lengthOfMonth()
            )

            selectedYear = year
            selectedMonth = month
            onDateSelected(LocalDate.of(year, month, day))
        }
    )
}

@Composable
fun TimeScrollPicker(
    initialHour24: Int = 14, // 24시간 형식
    initialMinute: Int = 30,
    onTimeSelected: (hour24: Int, minute: Int) -> Unit
) {
    val amPmList = listOf("AM", "PM")
    val minuteList = (0..59).toList()
    val hourList12 = (1..12).toList()

    val isPM = initialHour24 >= 12
    val hour12 = when {
        initialHour24 == 0 -> 12
        initialHour24 > 12 -> initialHour24 - 12
        else -> initialHour24
    }

    CommonScrollPicker(
        columns = listOf(
            PickerColumnConfig(
                items = amPmList,
                initialIndex = if (isPM) 1 else 0,
                labelFormatter = { "$it" }
            ),
            PickerColumnConfig(
                items = hourList12,
                initialIndex = hourList12.indexOf(hour12),
                labelFormatter = { "%02d".format(it) }
            ),
            PickerColumnConfig(
                items = minuteList,
                initialIndex = initialMinute,
                labelFormatter = { "%02d".format(it) }
            )
        ),
        onValuesSelected = { values ->
            val ampm = values[0] as String
            val hour = values[1] as Int
            val minute = values[2] as Int

            val hour24 = when {
                ampm == "AM" && hour == 12 -> 0
                ampm == "AM" -> hour
                ampm == "PM" && hour == 12 -> 12
                else -> hour + 12
            }

            onTimeSelected(hour24, minute)
        }
    )
}

@Composable
fun SingleNumberScrollPicker(
    numberRange: IntRange = 0..1000,
    initialNumber: Int,
    onNumberSelected: (Int) -> Unit
) {
    CommonScrollPicker(columns = listOf(
        PickerColumnConfig(
            items = numberRange.toList(),
            initialIndex = numberRange.indexOf(initialNumber),
            labelFormatter = { "$it" }
        )
    ), onValuesSelected = { values ->
        val number = values.first() as Int
        onNumberSelected(number)
    })

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DateScrollPickerPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.of(2023, 5, 5)) }

    PicplzTheme {
        DateScrollPicker(
            initialDate = selectedDate,
        ) { selectedDate = it }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TimeScrollPickerPreview() {
    var timeText by remember { mutableStateOf("") }

    PicplzTheme {
        TimeScrollPicker(
            initialHour24 = 15,
            initialMinute = 59
        ) { hour, minute ->
            timeText = "%02d:%02d".format(hour, minute)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SingleNumberScrollPickerPreview() {
    var count by remember { mutableIntStateOf(3) }

    PicplzTheme {
        SingleNumberScrollPicker(
            numberRange = 0..50,
            initialNumber = count,
        ) {
            count = it
        }
    }
}