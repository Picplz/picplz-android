package com.hm.picplz.ui.screen.quick_shoot.composable

import CommonStatusTag
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.core.ui.R
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

data class StatusTagData(
    val label: String,
    val iconResId: Int,
)

private val statusTags =
    listOf(
        StatusTagData("팔로우", R.drawable.tag_check),
        StatusTagData("빠른촬영 가능", R.drawable.tag_camera),
    )

private val vibeTags =
    listOf(
        "#을지로 감성",
        "#키치 감성",
        "#MZ 감성",
        "#퇴폐 감성",
    )

@Composable
fun PhotographerListSheet(
    photographers: FilteredPhotographers,
    selectedSortType: QuickShootSortType,
    onSortClick: () -> Unit,
    onPhotographerClick: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    val nestedScrollConnection =
        remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    val isAtTop =
                        listState.firstVisibleItemIndex == 0 &&
                            (listState.firstVisibleItemScrollOffset == 0)
                    return if (available.y > 0 && isAtTop) available else Offset.Zero
                }
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            statusTags.forEach { statusTag ->
                CommonStatusTag(
                    label = statusTag.label,
                    icon = painterResource(id = statusTag.iconResId),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onSortClick),
        ) {
            Text(
                text = selectedSortType.label,
                style =
                    TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 12.sp * 1.4,
                    ),
                color = MainThemeColor.Gray5,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "정렬 방식 선택",
                modifier = Modifier.size(8.dp),
                tint = MainThemeColor.Gray5,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        val allPhotographers = photographers.active + photographers.inactive

        LazyColumn(
            state = listState,
            modifier = Modifier.nestedScroll(nestedScrollConnection),
        ) {
            itemsIndexed(allPhotographers) { index, photographer ->
                PhotographerCard(
                    photographer = photographer,
                    onClick = { onPhotographerClick(photographer.id) },
                )
                if (index < allPhotographers.lastIndex) {
                    HorizontalDivider(
                        color = MainThemeColor.Gray2,
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun PhotographerListSheetPreview() {
    PhotographerListSheet(
        photographers =
            FilteredPhotographers(
                active =
                    listOf(
                        Photographer(
                            id = 1,
                            name = "작가1",
                            profileImageUri = "https://picsum.photos/200",
                            isActive = true,
                            distance = 100,
                            photoMoods = listOf("을지로 감성", "키치 감성"),
                        ),
                    ),
                inactive =
                    listOf(
                        Photographer(
                            id = 2,
                            name = "작가2",
                            profileImageUri = "https://picsum.photos/201",
                            isActive = false,
                            distance = 250,
                            photoMoods = listOf("MZ 감성"),
                        ),
                    ),
            ),
        selectedSortType = QuickShootSortType.DISTANCE,
        onSortClick = {},
        onPhotographerClick = {},
    )
}
