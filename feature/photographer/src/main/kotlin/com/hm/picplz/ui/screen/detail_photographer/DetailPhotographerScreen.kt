package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
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
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
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
                    onClick = { },
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
            viewModel.handleIntent(DetailPhotographerIntent.ToggleReportSheet)
        },
    )

    com.hm.picplz.ui.screen.detail_photographer.review.ReportBottomSheet(
        visible = state.isReportSheetVisible,
        onDismiss = {
            viewModel.handleIntent(DetailPhotographerIntent.ToggleReportSheet)
        },
        onSelect = { /* TODO: 신고 API 연동 */ },
    )

    // 커스텀 토스트 상태
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is DetailPhotographerSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
                is DetailPhotographerSideEffect.ShowBlockedToast -> {
                    toastMessage =
                        context.getString(
                            PhotographerR.string.blocked_toast,
                            sideEffect.name,
                        )
                }
            }
        }
    }

    // 토스트 자동 dismiss
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            kotlinx.coroutines.delay(TOAST_DURATION_MS)
            toastMessage = null
        }
    }

    // 커스텀 토스트 오버레이
    toastMessage?.let { message ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Text(
                text = message,
                style = MainThemeFont.Body,
                color = MainThemeColor.White,
                modifier =
                    Modifier
                        .background(
                            color = Color(0xFF0C0C0C).copy(alpha = 0.7f),
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 64.dp, vertical = 13.dp),
            )
        }
    }
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

private const val TOAST_DURATION_MS = 2000L

@Preview(showBackground = true)
@Composable
fun DetailPhotographerScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerScreen(navController = navController)
    }
}
