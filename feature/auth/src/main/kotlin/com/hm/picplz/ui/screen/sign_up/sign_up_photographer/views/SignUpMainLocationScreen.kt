package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.SignUpDevice
import com.hm.picplz.ui.screen.common.AreaTag
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonSearchField
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.Navigate
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable.AreaListItem
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpMainLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    signUpPhotographerNavController: NavController,
) {
    val currentState by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    var isSearchFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(currentState.searchQuery) {
        if (currentState.searchQuery.isNotBlank()) {
            delay(200L)
            viewModel.handleIntent(
                SignUpPhotographerIntent.SearchArea(currentState.searchQuery),
            )
        } else {
            viewModel.handleIntent(
                SignUpPhotographerIntent.SearchArea(""),
            )
        }
    }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .imePadding(),
        containerColor = MainThemeColor.White,
    ) {
            innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        focusManager.clearFocus()
                    },
        ) {
            CommonTopBar(
                text = "주 촬영지",
                onClickBack = { viewModel.handleIntent(NavigateToPrev) },
            )
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 15.dp)
                        .weight(1f),
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(top = 16.dp),
                    text = "주 촬영지를 선택해 주세요.",
                    style = MaterialTheme.typography.titleMedium,
                )
                CommonSearchField(
                    modifier = Modifier.padding(top = 30.dp),
                    value = currentState.searchQuery,
                    onValueChange = { searchText ->
                        viewModel.handleIntent(
                            SignUpPhotographerIntent.UpdateSearchQuery(searchText),
                        )
                    },
                    placeholder = "구 단위로 검색 (ex, 마포구)",
                    onSearchClick = {
                        viewModel.handleIntent(
                            SignUpPhotographerIntent.SearchArea(
                                currentState.searchQuery,
                            ),
                        )
                    },
                    keyboardActions = {
                        viewModel.handleIntent(
                            SignUpPhotographerIntent.SearchArea(
                                currentState.searchQuery,
                            ),
                        )
                    },
                    onFocusChanged = { isFocused ->
                        isSearchFieldFocused = isFocused
                    },
                )
                if (currentState.selectedAreas.isNotEmpty()) {
                    LazyRow(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(currentState.selectedAreas) { area ->
                            AreaTag(
                                label = area.name.split(" ").lastOrNull() ?: area.name,
                                onRemove = {
                                    focusManager.clearFocus()
                                    viewModel.handleIntent(
                                        SignUpPhotographerIntent.RemoveSelectedArea(area),
                                    )
                                },
                            )
                        }
                    }
                }

                val spacingToIconText = if (currentState.hasSearchCompleted) 40.dp else 20.dp
                Spacer(modifier = Modifier.height(spacingToIconText))

                val showIconText = !isSearchFieldFocused || currentState.hasSearchCompleted

                if (showIconText) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.marker_icon),
                            contentDescription = "아이콘",
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text =
                                when {
                                    currentState.searchQuery.isBlank() -> "인근 지역"
                                    else -> "'${currentState.searchQuery}' 검색 결과"
                                },
                            style = MainFontFamily.buttonDefault,
                            fontWeight = FontWeight.SemiBold,
                            color = MainThemeColor.Black,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                ) {
                    when {
                        currentState.isSearching -> {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        currentState.searchResults.isNotEmpty() -> {
                            Column {
                                LazyColumn(
                                    modifier = Modifier.padding(top = 16.dp),
                                ) {
                                    itemsIndexed(currentState.searchResults) { index, area ->
                                        val isSelected = currentState.selectedAreas.any { it.id == area.id }

                                        AreaListItem(
                                            area = area,
                                            isSelected = isSelected,
                                            onItemClick = { selectedArea ->
                                                focusManager.clearFocus()
                                                viewModel.handleIntent(
                                                    SignUpPhotographerIntent.ToggleAreaSelection(selectedArea),
                                                )
                                            },
                                        )
                                        if (index < currentState.searchResults.size - 1) {
                                            HorizontalDivider(
                                                thickness = 1.dp,
                                                color = MainThemeColor.Gray2,
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        currentState.searchQuery.isNotBlank() &&
                            currentState.searchResults.isEmpty() &&
                            !currentState.isSearching &&
                            currentState.hasSearchCompleted -> {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(),
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
                                        text = "검색 결과가 없어요",
                                        style = pretendardTypography.titleSmall,
                                        color = MainThemeColor.Gray6,
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.user_undefined),
                                        contentDescription = "아이콘",
                                        modifier =
                                            Modifier
                                                .height(60.68.dp)
                                                .width(52.39.dp),
                                    )
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Text(
                                        text = "다른 지역으로\n이동해 보는 건 어때요?",
                                        style = pretendardTypography.bodyMedium,
                                        color = MainThemeColor.Gray5,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier =
                    Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                CommonBottomButton(
                    text = "다음",
                    onClick = {
                        viewModel.handleIntent(Navigate(SignUpDevice))
                    },
                    enabled = currentState.selectedAreas.isNotEmpty(),
                )
            }
        }
        currentState.toastMessage?.let { message ->
            CommonToast(
                message = message,
                isVisible = currentState.showToast,
                onDismiss = {
                    viewModel.handleIntent(SignUpPhotographerIntent.DismissToast)
                },
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
                }
                is SignUpPhotographerSideEffect.Navigate -> {
                    signUpPhotographerNavController.navigate(sideEffect.destination)
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpMainLocationScreenPreview() {
    PicplzTheme {
        val mainNavController = rememberNavController()
        val signUpPhotographerNavController = rememberNavController()

        SignUpMainLocationScreen(
            mainNavController = mainNavController,
            signUpPhotographerNavController = signUpPhotographerNavController,
        )
    }
}
