package com.hm.picplz.ui.screen.my_page

import ChipHeight
import CommonChip
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hm.picplz.common.model.ChipItem
import com.hm.picplz.common.model.ChipMode
import com.hm.picplz.feature.mypage.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.SetStatusBarStyle
import kotlinx.coroutines.flow.collectLatest

private const val ADD_KEYWORD_CHIP_ID = "ADD_KEYWORD"

@Composable
fun MyPagePhotographerKeywordEditRoute(
    navController: NavHostController,
    photographerId: Long,
    modifier: Modifier = Modifier,
    viewModel: MyPagePhotographerKeywordEditViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val defaultKeywords = stringArrayResource(R.array.my_page_default_mood_keywords).toList()

    SetStatusBarStyle()

    LaunchedEffect(photographerId, defaultKeywords) {
        viewModel.handleIntent(
            MyPagePhotographerKeywordEditIntent.LoadKeywords(
                photographerId = photographerId,
                defaultKeywords = defaultKeywords,
            ),
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is MyPagePhotographerKeywordEditSideEffect.NavigateToPrev -> {
                    sideEffect.keywordSummary?.let { keywordSummary ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(KEY_PHOTOGRAPHER_KEYWORD_SUMMARY, keywordSummary)
                    }
                    navController.popBackStack()
                }
            }
        }
    }

    MyPagePhotographerKeywordEditScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyPagePhotographerKeywordEditScreen(
    state: MyPagePhotographerKeywordEditState,
    onIntent: (MyPagePhotographerKeywordEditIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        onIntent(MyPagePhotographerKeywordEditIntent.SetEditingChipId(null))
                    })
                },
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommonTopBar(
                text = stringResource(R.string.my_page_keyword_edit_title),
                onClickBack = { onIntent(MyPagePhotographerKeywordEditIntent.NavigateToPrev) },
            )
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                        .imePadding()
                        .verticalScroll(scrollState),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(80.dp))
                    Text(
                        text = stringResource(R.string.my_page_keyword_edit_description),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        state.keywordChipList.forEach { chip ->
                            CommonChip(
                                id = chip.id,
                                label = chip.label,
                                initialMode = ChipMode.DEFAULT,
                                isEditing = state.editingChipId == chip.id,
                                isEditable = chip.isEditable,
                                onClickDefaultMode = {
                                    focusManager.clearFocus()
                                    onIntent(MyPagePhotographerKeywordEditIntent.SetEditingChipId(null))
                                    onIntent(
                                        MyPagePhotographerKeywordEditIntent.ToggleKeywordSelection(
                                            chipId = chip.id,
                                            label = chip.label,
                                        ),
                                    )
                                },
                                isSelected = state.selectedKeywordChipList.any { it.id == chip.id },
                                onUpdate = { value ->
                                    onIntent(
                                        MyPagePhotographerKeywordEditIntent.UpdateKeywordChip(
                                            chip.id,
                                            value,
                                        ),
                                    )
                                },
                                onEdit = {
                                    onIntent(
                                        MyPagePhotographerKeywordEditIntent.SetEditingChipId(chip.id),
                                    )
                                },
                                onEndEdit = {
                                    focusManager.clearFocus()
                                    onIntent(
                                        MyPagePhotographerKeywordEditIntent.SetEditingChipId(null),
                                    )
                                },
                                height = ChipHeight.BIG,
                            )
                        }
                        CommonChip(
                            id = ADD_KEYWORD_CHIP_ID,
                            initialMode = ChipMode.ADD,
                            isEditing = state.editingChipId == ADD_KEYWORD_CHIP_ID,
                            onEdit = {
                                onIntent(
                                    MyPagePhotographerKeywordEditIntent.SetEditingChipId(ADD_KEYWORD_CHIP_ID),
                                )
                            },
                            onAdd = { value ->
                                onIntent(MyPagePhotographerKeywordEditIntent.AddKeywordChip(value))
                                onIntent(MyPagePhotographerKeywordEditIntent.SetEditingChipId(null))
                            },
                            height = ChipHeight.BIG,
                        )
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
                    text = stringResource(R.string.my_page_keyword_edit_save),
                    onClick = { onIntent(MyPagePhotographerKeywordEditIntent.SaveKeywords) },
                    enabled = state.hasChanges && !state.isSaving && !state.isLoading,
                )
            }
        }
        state.toastMessageResId?.let { messageResId ->
            CommonToast(
                message = stringResource(messageResId),
                isVisible = state.showToast,
                onDismiss = { onIntent(MyPagePhotographerKeywordEditIntent.DismissToast) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePhotographerKeywordEditScreenPreview() {
    PicplzTheme {
        MyPagePhotographerKeywordEditScreen(
            state =
                MyPagePhotographerKeywordEditState(
                    keywordChipList =
                        listOf(
                            ChipItem(id = "1", label = "캐주얼"),
                            ChipItem(id = "2", label = "심플"),
                        ),
                    selectedKeywordChipList =
                        listOf(ChipItem(id = "1", label = "캐주얼")),
                ),
            onIntent = {},
        )
    }
}
