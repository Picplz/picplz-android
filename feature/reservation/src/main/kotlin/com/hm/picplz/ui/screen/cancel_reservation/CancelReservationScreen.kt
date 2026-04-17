package com.hm.picplz.ui.screen.cancel_reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReasonInputContent
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReservationStepIndicator
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReservationTopBar
import com.hm.picplz.ui.screen.common.CommonBottomButton

@Composable
fun CancelReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCancelConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CancelReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState =
        rememberPagerState(initialPage = state.currentPagerPage.ordinal) {
            CancelReservationPagerPage.size
        }

    LaunchedEffect(state.currentPagerPage) {
        pagerState.animateScrollToPage(state.currentPagerPage.ordinal)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is CancelReservationSideEffect.NavigateBack -> {
                    onNavigateBack()
                }

                is CancelReservationSideEffect.ShowCancelConfirmModal -> {
                    onNavigateToCancelConfirm()
                }
            }
        }
    }

    CancelReservationScreenContent(
        modifier = modifier,
        state = state,
        pagerState = pagerState,
        onBackClick = {
            viewModel.handleIntent(CancelReservationIntent.OnBackClick)
        },
        onReasonToggle = { reason ->
            viewModel.handleIntent(CancelReservationIntent.ToggleReason(reason))
        },
        onDirectInputChange = { text ->
            viewModel.handleIntent(CancelReservationIntent.UpdateDirectInput(text))
        },
        onPageChange = { pagerPage ->
            viewModel.handleIntent(CancelReservationIntent.UpdatePagerPage(pagerPage))
        },
        onNextClick = {
            viewModel.handleIntent(CancelReservationIntent.OnNextClick)
        },
    )
}

@Composable
private fun CancelReservationScreenContent(
    state: CancelReservationState,
    pagerState: PagerState,
    onBackClick: () -> Unit,
    onReasonToggle: (CancelReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    onPageChange: (CancelReservationPagerPage) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            CancelReservationTopBar(
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
        ) {
            CancelReservationStepIndicator(
                modifier =
                    Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
                currentPage = state.currentPagerPage,
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false,
            ) { page ->
                when (CancelReservationPagerPage.fromIndex(page)) {
                    CancelReservationPagerPage.REASON_INPUT -> {
                        CancelReasonInputContent(
                            selectedReasons = state.selectedReasons,
                            directInputText = state.directInputText,
                            onReasonToggle = onReasonToggle,
                            onDirectInputChange = onDirectInputChange,
                        )
                    }

                    CancelReservationPagerPage.REFUND_GUIDE -> {
                        RefundGuideContent(
                            selectedReasons = state.selectedReasons,
                            directInputText = state.directInputText,
                        )
                    }
                }
            }

            CommonBottomButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                text =
                    when (state.currentPagerPage) {
                        CancelReservationPagerPage.REASON_INPUT -> "다음"
                        CancelReservationPagerPage.REFUND_GUIDE -> "취소 확인"
                    },
                onClick = onNextClick,
                enabled = state.isNextButtonEnabled(),
            )
        }
    }
}

@Composable
private fun RefundGuideContent(
    selectedReasons: Set<CancelReason>,
    directInputText: String,
    modifier: Modifier = Modifier,
) {
    // TODO: 환불 안내 화면 구현
}

@Preview
@Composable
private fun CancelReservationScreenPreview() {
    CancelReservationScreenContent(
        state =
            CancelReservationState(
                orderId = "order123",
                selectedReasons = setOf(CancelReason.SCHEDULE),
                directInputText = "",
                currentPagerPage = CancelReservationPagerPage.REASON_INPUT,
            ),
        pagerState = rememberPagerState(initialPage = 0) { CancelReservationPagerPage.size },
        onBackClick = {},
        onReasonToggle = {},
        onDirectInputChange = {},
        onPageChange = {},
        onNextClick = {},
    )
}
