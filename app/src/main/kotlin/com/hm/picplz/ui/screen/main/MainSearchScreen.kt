package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.main.modalBottomSheet.DeviceModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.MoodKeywordModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortFilterModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import com.hm.picplz.ui.screen.main.search.SearchField
import com.hm.picplz.ui.screen.main.search.SearchFilterButton
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable.AreaTag
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

// 3가지 상태를 표현하는 sealed class
sealed class SearchUiState {
    object Empty : SearchUiState()              // 아무 입력도 없는 초기 상태
    object Typing : SearchUiState()              // 포커스가 있고 입력 중인 상태
    data class Complete(                                // 완료(검색) 버튼을 눌러야 보여줄 상태
        val query: String
    ) : SearchUiState()
}

@Composable
fun Header(
    navController: NavHostController,
    uiState: SearchUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    var visibleRegion by remember { mutableStateOf(false) }
    var visibleDevice by remember { mutableStateOf(false) }
    var visibleDeviceMood by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .zIndex(1f)
            .wrapContentSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.triangle_left),
                    contentDescription = "arrow left",
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(17.dp))
                SearchField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = "촬영을 하고 싶은 장소 또는 동을 검색해보세요",
                    modifier = Modifier.height(46.dp),
                    onSearchClick = onSearchClick,
                    keyboardActions = {
                        onSearchClick()
                    },
                    onFocusChanged = onFocusChanged
                )
            }

            if (uiState is SearchUiState.Complete) {
                LazyRow(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    item {
                        SearchFilterButton(
                            label = "촬영 지역",
                            isSelected = false,
                            onClick = { visibleRegion = true })
                    }
                    item {
                        SearchFilterButton(
                            label = "촬영 기기",
                            isSelected = false,
                            onClick = { visibleDevice = true })

                    }
                    item {
                        SearchFilterButton(
                            label = "분위기 태그",
                            isSelected = false,
                            onClick = { visibleDeviceMood = true })

                    }
                }

                RegionModalBottomSheet(
                    onDismiss = { visibleRegion = false },
                    visible = visibleRegion,
                )

                DeviceModalBottomSheet(
                    onDismiss = { visibleDevice = false },
                    visible = visibleDevice
                )

                MoodKeywordModalBottomSheet(
                    onDismiss = { visibleDeviceMood = false },
                    visible = visibleDeviceMood
                )
            }
        }
    }
}


@Composable
fun RecentSearchSection(
    recentSearchQueries: SnapshotStateList<String>,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(recentSearchQueries.size) {
        if (recentSearchQueries.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    if (recentSearchQueries.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "최근 검색어",
                style = MainThemeFont.ButtonDefault
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "모두 삭제",
                style = MainThemeFont.InnerTag,
                color = MainThemeColor.Gray4,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    focusManager.clearFocus()
                    onClearAll()
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (recentSearchQueries.isNotEmpty()) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(
                    items = recentSearchQueries,
                    key = { it }
                ) { area ->
                    AreaTag(
                        label = area,
                        onRemove = {
                            focusManager.clearFocus()
                            onRemove(area)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun PopularSpotSection(
    popularSpots: List<String>,
    onSpotClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "인기 촬영지",
                style = MainThemeFont.ButtonDefault
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "04.22 12:00 기준",
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray3,
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        popularSpots.forEachIndexed { index, spot ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSpotClick(spot) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}",
                    style = MainThemeFont.ButtonDefault
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = spot,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5
                )
            }
        }
    }
}

@Composable
fun PhotographerListItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MainThemeColor.Olive)
    ) {

    }
}

@Composable
fun SearchResultSection() {
    var visibleSortFilter by remember { mutableStateOf(false) }
    var selectedSortType by remember { mutableStateOf(SortType.POPULAR) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        // TODO: 폰트시스템 추가
        CommonIconButton(
            label = selectedSortType.label,
            backgroundColor = MainThemeColor.Transparent,
            textColor = MainThemeColor.Gray5,
            iconResId = R.drawable.arrow_down,
            horizontalPadding = 0.dp,
            location = "right",
            onClick = { visibleSortFilter = true }
        )

        // 검색 결과가 있을 때
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
//            TODO: 리스트 연결
            item {
                PhotographerListItem()
                HorizontalDivider(
                    color = MainThemeColor.Gray2,
                    thickness = 1.dp,
                )
            }
            item {
                PhotographerListItem()
                HorizontalDivider(
                    color = MainThemeColor.Gray2,
                    thickness = 1.dp,
                )
            }
            item {
                PhotographerListItem()
                HorizontalDivider(
                    color = MainThemeColor.Gray2,
                    thickness = 1.dp,
                )
            }
            item {
                PhotographerListItem()
                HorizontalDivider(
                    color = MainThemeColor.Gray2,
                    thickness = 1.dp,
                )
            }
        }

        // 검색 결과가 없을 때
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 163.dp), contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "검색 결과가 없습니다", style = MainThemeFont.TitleSmall)
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.user_undefined),
                    contentDescription = "user-undefined"
                )
            }
        }

        SortFilterModalBottomSheet(
            onDismiss = { visibleSortFilter = false },
            visible = visibleSortFilter,
            onSelect = { selectedType ->
                selectedSortType = selectedType
            }
        )
    }
}

@Composable
fun TypingResultSection(
    onSuggestionClick: (String) -> Unit

) {
    // 더미 데이터
    val dummyList = listOf(
        mapOf("type" to "location", "label" to "서울 강남구 도곡동"),
        mapOf("type" to "location", "label" to "서울 강남구 도곡1동"),
        mapOf("type" to "location", "label" to "서울 강남구 도곡2동"),
        mapOf("type" to "photographer", "profileImage" to R.drawable.logo, "label" to "강주은 작가"),
        mapOf("type" to "photographer", "profileImage" to R.drawable.logo, "label" to "강아지 작가"),
        mapOf("type" to "tag", "label" to "강아지랑 촬영"),
        mapOf("type" to "tag", "label" to "강한 컨셉")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        items(dummyList) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionClick(item["label"].toString()) }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (item["type"]) {
                    "location" -> {
                        Icon(
                            painter = painterResource(id = R.drawable.marker_map_gray),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MainThemeColor.Gray3
                        )
                        Text(
                            text = item["label"].toString(),
                            style = MainThemeFont.Body,
                        )
                    }

                    "photographer" -> {
                        Image(
                            painter = painterResource(id = R.drawable.user_undefined),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = item["label"].toString(),
                            style = MainThemeFont.BodyBold,
                        )
                    }

                    "tag" -> {
                        Text(
                            text = item["label"].toString(),
                            style = MainThemeFont.Body,
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 0.96.dp, color = MainThemeColor.Gray2)
        }
    }
}

@Composable
fun MainSearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var query by rememberSaveable { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }

    val recentSearchQueries = remember { mutableStateListOf("연희동", "성수", "홍익대", "연남동", "송파구") }
    val popularSpots = listOf("성수동", "연남동", "서교동", "합정동", "망원동")
    val focusManager = LocalFocusManager.current


    val doSearch: (String) -> Unit = { newQuery ->
        query = newQuery
        hasSearched = true
        focusManager.clearFocus()
        if (newQuery.isNotBlank() && !recentSearchQueries.contains(newQuery)) {
            recentSearchQueries.add(0, newQuery)
        }
    }


    val uiState: SearchUiState = when {
        hasSearched -> SearchUiState.Complete(query)
        isFocused && query.isNotEmpty() -> SearchUiState.Typing
        else -> SearchUiState.Empty
    }

    Scaffold(
        containerColor = MainThemeColor.White,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Header(
                navController = navController,
                uiState = uiState,
                query = query,
                onQueryChange = {
                    query = it
                    hasSearched = false
                },
                onSearchClick = { doSearch(query) },
                onFocusChanged = { isFocused = it }
            )

            when (uiState) {
                is SearchUiState.Empty -> {
                    Spacer(modifier = Modifier.height(20.dp))

                    RecentSearchSection(
                        recentSearchQueries = recentSearchQueries,
                        onRemove = { area ->
                            focusManager.clearFocus()
                            recentSearchQueries.remove(area)
                        },
                        onClearAll = {
                            focusManager.clearFocus()
                            recentSearchQueries.clear()
                        }
                    )

                    PopularSpotSection(
                        popularSpots = popularSpots,
                        onSpotClick = { spot -> doSearch(spot) }
                    )
                }

                is SearchUiState.Complete -> {
                    Spacer(modifier = Modifier.height(20.dp))

                    SearchResultSection()
                }

                SearchUiState.Typing -> {
                    Spacer(modifier = Modifier.height(20.dp))

                    TypingResultSection(
                        onSuggestionClick = { sug -> doSearch(sug) }
                    )
                }
            }


        }
    }
}