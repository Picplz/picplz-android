package com.hm.picplz.ui.screen.main.modalBottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.screen.common.CommonScrollableTabRow
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionModalBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
//    TODO: State, ViewModel 연결
    val tabTitles = listOf("서울", "경기", "인천", "부산", "제주")
    val regions = mapOf(
        "서울" to mapOf(
            "강남구" to listOf("신사동", "논현동", "압구정동", "청담동"),
            "강동구" to listOf("천호동", "명일동", "고덕동"),
            "강북구" to listOf("수유동", "미아동"),
            "강서구" to listOf("화곡동", "방화동"),
            "광진구" to listOf("구의동", "자양동"),
            "구로구" to listOf("구로동", "가리봉동"),
            "동작구" to listOf("상도동", "사당동"),
            "서초구" to listOf("서초동", "반포동"),
            "송파구" to listOf("잠실동", "문정동")
        ),
        "경기" to mapOf("성남시" to listOf("분당동", "정자동")),
        "인천" to mapOf("연수구" to listOf("송도동")),
        "부산" to mapOf("해운대구" to listOf("우동", "좌동")),
        "제주" to mapOf("제주시" to listOf("이도동"))
    )

    var selectedTab by remember { mutableIntStateOf(0) }
    val selectedRegion = tabTitles[selectedTab]
    val districts = regions[selectedRegion]?.keys?.toList().orEmpty()
    var selectedDistrict by remember { mutableStateOf(districts.firstOrNull().orEmpty()) }
    val realSubDistricts = regions[selectedRegion]?.get(selectedDistrict).orEmpty()
    val subDistricts = listOf("$selectedDistrict 전체") + realSubDistricts
    var selectedRegions by remember { mutableStateOf(listOf<String>()) }

    fun toggleRegion(region: String) {
        selectedRegions = when (region) {
            "$selectedDistrict 전체" -> {
                val allSelected = realSubDistricts.all { it in selectedRegions }
                if (allSelected) selectedRegions - realSubDistricts.toSet()
                else selectedRegions + realSubDistricts.filterNot { it in selectedRegions }
            }

            in selectedRegions -> selectedRegions - region
            else -> selectedRegions + region
        }
    }

    fun isRegionChecked(region: String): Boolean =
        if (region == "$selectedDistrict 전체") realSubDistricts.all { it in selectedRegions }
        else region in selectedRegions

    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = true,
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetMaxHeight = 724.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()

            CommonScrollableTabRow(
                isIndicatorMatchTextWidth = true,
                selectedTabIndex = selectedTab,
                tabTitles = tabTitles,
                onTabSelected = { index ->
                    selectedTab = index
                    selectedDistrict = regions[tabTitles[index]]?.keys?.firstOrNull().orEmpty()
                },
                textStyle = MainThemeFont.BodyBold
            )

            Box(modifier = Modifier.weight(1f)) {
                RegionSelector(
                    districts = districts,
                    selectedDistrict = selectedDistrict,
                    onDistrictSelected = { selectedDistrict = it },
                    subDistricts = subDistricts,
                    toggleRegion = ::toggleRegion,
                    isRegionChecked = ::isRegionChecked
                )
            }

            SelectedRegionChips(
                selectedRegions = selectedRegions,
                onRemove = { selectedRegions = selectedRegions - it }
            )

            Footer(
                onReset = { selectedRegions = emptyList() },
                onSubmit = { /* TODO: 작가 search API 연동 */ }
            )
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "지역",
            style = MainThemeFont.TitleSmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RegionSelector(
    districts: List<String>,
    selectedDistrict: String,
    onDistrictSelected: (String) -> Unit,
    subDistricts: List<String>,
    toggleRegion: (String) -> Unit,
    isRegionChecked: (String) -> Boolean
) {
    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MainThemeColor.Gray1)
        ) {
            items(districts) { district ->
                val isSelected = selectedDistrict == district
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelected) MainThemeColor.White else MainThemeColor.Gray1)
                        .clickable { onDistrictSelected(district) }
                        .padding(16.dp),
                ) {
                    Text(
                        text = district,
                        style = MainThemeFont.Body.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray4
                        )
                    )
                    if (isSelected) {
                        Image(
                            painter = painterResource(id = R.drawable.region_modal_depth_arrow),
                            contentDescription = "depth-arrow"
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            items(subDistricts) { sub ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toggleRegion(sub) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        sub,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MainThemeFont.Body
                    )
                    Checkbox(
                        checked = isRegionChecked(sub),
                        onCheckedChange = { toggleRegion(sub) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MainThemeColor.Black,
                            uncheckedColor = MainThemeColor.Gray3
                        )
                    )
                }
                HorizontalDivider(color = MainThemeColor.Gray2, thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun SelectedRegionChips(
    selectedRegions: List<String>,
    onRemove: (String) -> Unit
) {
    if (selectedRegions.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 21.dp, top = 19.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = MainThemeFont.ButtonDefault.toSpanStyle()) {
                    append("선택한 지역 ")
                }
                withStyle(
                    MainThemeFont.ButtonDefault.toSpanStyle().copy(color = MainThemeColor.Olive)
                ) {
                    append(selectedRegions.size.toString())
                }
            }
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(selectedRegions) { region ->
                AssistChip(
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(1.dp, MainThemeColor.Gray4),
                    onClick = { onRemove(region) },
                    label = { Text(region, style = MainThemeFont.BodyBold) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "삭제"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun Footer(
    onReset: () -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 45.dp, top = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CommonBottomButton(
            text = "초기화",
            onClick = onReset,
            modifier = Modifier.weight(1f),
            contentColor = MainThemeColor.Black,
            borderColor = MainThemeColor.Gray3,
            containerColor = MainThemeColor.White
        )
        CommonBottomButton(
            text = "{}명 작가보기",
            onClick = onSubmit,
            modifier = Modifier.weight(2f)
        )
    }
}
