package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.domain.model.Area
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.AreaTag
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.common.CommonSearchField
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPagePhotographerActiveAreaEditRoute(
    photographerId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyPagePhotographerActiveAreaEditViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(photographerId) {
        viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.LoadActiveAreas(photographerId))
    }

    LaunchedEffect(state.searchQuery) {
        delay(200L)
        viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.SearchArea)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                MyPagePhotographerActiveAreaEditSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    MyPagePhotographerActiveAreaEditScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@Composable
fun MyPagePhotographerActiveAreaEditScreen(
    state: MyPagePhotographerActiveAreaEditState,
    onIntent: (MyPagePhotographerActiveAreaEditIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val toastMessage = state.toastMessageResId?.let { stringResource(it) }.orEmpty()

    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(
                title = stringResource(R.string.active_area_edit_title),
                onClickBack = { onIntent(MyPagePhotographerActiveAreaEditIntent.NavigateBack) },
            )
        },
        bottomBar = {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                CommonBottomButton(
                    text = stringResource(R.string.active_area_edit_submit),
                    onClick = { onIntent(MyPagePhotographerActiveAreaEditIntent.Save) },
                    enabled = state.isSaveEnabled,
                )
            }
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.active_area_edit_description),
                    style = MainThemeFont.TitleSmall,
                    color = MainThemeColor.Black,
                    modifier = Modifier.padding(top = 16.dp),
                )

                CommonSearchField(
                    value = state.searchQuery,
                    onValueChange = { query ->
                        onIntent(MyPagePhotographerActiveAreaEditIntent.UpdateSearchQuery(query))
                    },
                    placeholder = stringResource(R.string.active_area_edit_search_placeholder),
                    onSearchClick = {
                        focusManager.clearFocus()
                        onIntent(MyPagePhotographerActiveAreaEditIntent.SearchArea)
                    },
                    keyboardActions = {
                        focusManager.clearFocus()
                        onIntent(MyPagePhotographerActiveAreaEditIntent.SearchArea)
                    },
                    modifier = Modifier.padding(top = 30.dp),
                )

                if (state.selectedAreas.isNotEmpty()) {
                    LazyRow(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.selectedAreas) { area ->
                            AreaTag(
                                label = area.displayName,
                                onRemove = {
                                    focusManager.clearFocus()
                                    onIntent(MyPagePhotographerActiveAreaEditIntent.RemoveSelectedArea(area))
                                },
                            )
                        }
                    }
                }

                ActiveAreaEditContent(
                    state = state,
                    onAreaClick = { area ->
                        focusManager.clearFocus()
                        onIntent(MyPagePhotographerActiveAreaEditIntent.ToggleAreaSelection(area))
                    },
                    modifier = Modifier.padding(top = 24.dp),
                )
            }

            CommonToast(
                message = toastMessage,
                isVisible = state.showToast,
                onDismiss = { onIntent(MyPagePhotographerActiveAreaEditIntent.DismissToast) },
            )
        }
    }
}

@Composable
private fun ActiveAreaEditContent(
    state: MyPagePhotographerActiveAreaEditState,
    onAreaClick: (Area) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading -> {
            LoadingMessage(
                message = stringResource(R.string.active_area_edit_loading),
                modifier = modifier,
            )
        }
        state.isSearching -> {
            LoadingMessage(
                message = stringResource(R.string.active_area_edit_searching),
                modifier = modifier,
            )
        }
        state.searchResults.isNotEmpty() -> {
            LazyColumn(modifier = modifier.fillMaxWidth()) {
                itemsIndexed(state.searchResults) { index, area ->
                    ActiveAreaListItem(
                        area = area,
                        isSelected = state.selectedAreas.any { it.id == area.id },
                        onClick = onAreaClick,
                    )
                    if (index < state.searchResults.lastIndex) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MainThemeColor.Gray2,
                        )
                    }
                }
            }
        }
        state.hasSearchCompleted -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.active_area_edit_empty_result),
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray4,
                )
            }
        }
    }
}

@Composable
private fun ActiveAreaListItem(
    area: Area,
    isSelected: Boolean,
    onClick: (Area) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = area.name,
        style = if (isSelected) MainThemeFont.BodyBold else MainThemeFont.Body,
        color = MainThemeColor.Black,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(area) }
                .padding(vertical = 16.dp),
    )
}

@Composable
private fun LoadingMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = MainThemeColor.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePhotographerActiveAreaEditScreenPreview() {
    PicplzTheme {
        MyPagePhotographerActiveAreaEditScreen(
            state =
                MyPagePhotographerActiveAreaEditState(
                    selectedAreas =
                        listOf(
                            Area(id = 1L, name = "서울 마포구 연남동", dong = "연남동", ri = null),
                            Area(id = 2L, name = "서울 서대문구 연희동", dong = "연희동", ri = null),
                        ),
                    searchResults =
                        listOf(
                            Area(id = 3L, name = "서울 마포구 합정동", dong = "합정동", ri = null),
                        ),
                    hasSearchCompleted = true,
                ),
            onIntent = {},
        )
    }
}
