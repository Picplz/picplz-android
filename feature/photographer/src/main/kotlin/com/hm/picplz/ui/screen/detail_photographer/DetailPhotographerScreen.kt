package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonButtonModal
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.detail_photographer.review.ReportBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun DetailPhotographerScreen(
    viewModel: DetailPhotographerViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val paddingModifier = Modifier.padding(horizontal = 15.dp)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text =
                    stringResource(
                        R.string.photographer_name_format,
                        state.profileInfo.name,
                    ),
                onClickBack = {
                    viewModel.handleIntent(DetailPhotographerIntent.NavigateToPrev)
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.handleIntent(
                                DetailPhotographerIntent.ToggleMenuSheet,
                            )
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = stringResource(R.string.menu),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                },
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                CommonBottomButton(
                    text =
                        if (state.profileInfo.isBookable) {
                            stringResource(R.string.booking_button)
                        } else {
                            stringResource(R.string.booking_unavailable)
                        },
                    onClick = {
                        viewModel.handleIntent(DetailPhotographerIntent.SelectBooking)
                    },
                    enabled = state.profileInfo.isBookable,
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                if (state.isBlocked) {
                    BlockedBanner(
                        onUnblock = {
                            viewModel.handleIntent(DetailPhotographerIntent.ToggleBlock)
                        },
                    )
                }

                DetailProfileSection(
                    modifier = paddingModifier,
                    profileInfo = state.profileInfo,
                    isFollow = state.isFollow,
                    isInfoExpanded = state.isInfoExpanded,
                    isAreaExpanded = state.isAreaExpanded,
                    onToggleFollow = {
                        viewModel.handleIntent(DetailPhotographerIntent.ToggleFollow)
                    },
                    onToggleInfoExpanded = {
                        viewModel.handleIntent(DetailPhotographerIntent.ToggleInfoExpanded)
                    },
                    onToggleAreaExpanded = {
                        viewModel.handleIntent(DetailPhotographerIntent.ToggleAreaExpanded)
                    },
                )

                SectionDivider()

                ReviewSection(
                    modifier = paddingModifier,
                    navController = navController,
                    reviewSummary = state.reviewSummary,
                    reviews = state.reviews,
                    photographerId = viewModel.photographerId,
                    onReport = {
                        viewModel.handleIntent(
                            DetailPhotographerIntent.ToggleReportSheet,
                        )
                    },
                )

                ThinDivider()

                PortfolioSection(
                    modifier = paddingModifier,
                    navController = navController,
                    photoPortfolios = state.profileInfo.photoPortfolios,
                    photographerId = viewModel.photographerId,
                )

                ThinDivider()

                Spacer(modifier = Modifier.height(20.dp))

                ShootingPackageSection(
                    modifier = paddingModifier,
                    packages = state.shootingPackages,
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        },
    )

    KebabMenuBottomSheet(
        visible = state.isMenuSheetVisible,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleMenuSheet)
        },
        onBlock = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleBlock)
        },
        onReport = {
            if (state.isPreviewMode) {
                viewModel.handleIntent(DetailPhotographerIntent.ToggleReportSheet)
            }
        },
    )

    // 리뷰 신고 바텀시트
    ReportBottomSheet(
        visible = state.isReportSheetVisible,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleReportSheet)
        },
        onSelect = { /* TODO: 신고 API 연동 */ },
    )

    state.previewActionDialog?.let { action ->
        DetailPreviewActionDialog(
            action = action,
            onDismiss = {
                viewModel.handleIntent(DetailPhotographerIntent.DismissPreviewActionDialog)
            },
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is DetailPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }

    CommonToast(
        message = state.toastMessage ?: "",
        isVisible = state.toastMessage != null,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.DismissToast)
        },
    )
}

@Composable
private fun DetailPreviewActionDialog(
    action: DetailPreviewAction,
    onDismiss: () -> Unit,
) {
    CommonButtonModal(
        onDismissRequest = onDismiss,
        cancelText = stringResource(PhotographerR.string.preview_action_dialog_cancel),
        confirmText = stringResource(PhotographerR.string.preview_action_dialog_confirm),
        onCancel = onDismiss,
        onConfirm = onDismiss,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(PhotographerR.string.preview_action_dialog_title),
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(previewActionDescriptionResId(action)),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun previewActionDescriptionResId(action: DetailPreviewAction): Int =
    when (action) {
        DetailPreviewAction.Booking -> PhotographerR.string.preview_action_dialog_booking
        DetailPreviewAction.Follow -> PhotographerR.string.preview_action_dialog_follow
        DetailPreviewAction.Block -> PhotographerR.string.preview_action_dialog_block
        DetailPreviewAction.Report -> PhotographerR.string.preview_action_dialog_report
    }

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        thickness = 10.dp,
        color = MainThemeColor.Gray1,
    )
}

@Composable
private fun ThinDivider() {
    HorizontalDivider(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        thickness = 1.dp,
        color = MainThemeColor.Gray1,
    )
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerScreen(navController = navController)
    }
}
