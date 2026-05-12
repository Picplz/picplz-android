package com.hm.picplz.ui.screen.my_page

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.domain.model.Area
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.ActiveAreaListItem
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
import com.hm.picplz.core.ui.R as CoreR

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
        if (state.searchQuery.isNotBlank()) {
            delay(200L)
        }
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
    val activity = LocalContext.current.findActivity()
    val toastMessage = state.toastMessageResId?.let { stringResource(it) }.orEmpty()

    DisposableEffect(activity) {
        val previousSoftInputMode = activity?.window?.attributes?.softInputMode
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        onDispose {
            previousSoftInputMode?.let { softInputMode ->
                activity.window.setSoftInputMode(softInputMode)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
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
                            .height(112.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                ) {
                    if (state.selectedAreas.isNotEmpty()) {
                        LazyRow(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .padding(bottom = 64.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(state.selectedAreas) { area ->
                                AreaTag(
                                    label = area.name.split(" ").lastOrNull() ?: area.name,
                                    onRemove = {
                                        focusManager.clearFocus()
                                        onIntent(MyPagePhotographerActiveAreaEditIntent.RemoveSelectedArea(area))
                                    },
                                )
                            }
                        }
                    }

                    CommonBottomButton(
                        text = stringResource(R.string.active_area_edit_submit),
                        onClick = { onIntent(MyPagePhotographerActiveAreaEditIntent.Save) },
                        enabled = state.isSaveEnabled,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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

                    ActiveAreaEditContent(
                        state = state,
                        onAreaClick = { area ->
                            focusManager.clearFocus()
                            onIntent(MyPagePhotographerActiveAreaEditIntent.ToggleAreaSelection(area))
                        },
                        modifier = Modifier.padding(top = 20.dp),
                    )
                }
            }
        }

        CommonToast(
            message = toastMessage,
            isVisible = state.showToast,
            bottomOffset = 120.dp,
            onDismiss = { onIntent(MyPagePhotographerActiveAreaEditIntent.DismissToast) },
        )
    }
}

@Composable
private fun ActiveAreaEditContent(
    state: MyPagePhotographerActiveAreaEditState,
    onAreaClick: (Area) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ActiveAreaResultHeader(
            title =
                if (state.searchQuery.isBlank()) {
                    stringResource(R.string.active_area_edit_nearby_title)
                } else {
                    "'${state.searchQuery}' ${stringResource(R.string.active_area_edit_search_result_suffix)}"
                },
            isVisible = state.searchQuery.isBlank() || state.hasSearchCompleted,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            when {
                state.isLoading || state.isSearching -> {
                    LoadingMessage()
                }
                state.searchResults.isNotEmpty() -> {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(state.searchResults) { index, area ->
                            ActiveAreaListItem(
                                label = area.name,
                                isSelected = state.selectedAreas.any { it.id == area.id },
                                onClick = { onAreaClick(area) },
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
                    EmptyAreaSearchResult()
                }
            }
        }
    }
}

@Composable
private fun ActiveAreaResultHeader(
    title: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(20.dp),
    ) {
        if (isVisible) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(CoreR.drawable.marker_icon),
                    contentDescription = stringResource(R.string.active_area_edit_result_marker),
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MainThemeFont.BodyBold,
                    color = MainThemeColor.Black,
                )
            }
        }
    }
}

@Composable
private fun EmptyAreaSearchResult(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .requiredHeight(166.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.active_area_edit_empty_result),
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Gray6,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(CoreR.drawable.user_undefined),
                contentDescription = stringResource(R.string.active_area_edit_empty_result_image),
                modifier =
                    Modifier
                        .height(60.68.dp)
                        .width(52.39.dp),
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.active_area_edit_empty_result_suggestion),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray5,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LoadingMessage(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
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
