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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReasonInputContent
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReservationStepIndicator
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReservationTopBar
import com.hm.picplz.ui.screen.cancel_reservation.composable.RefundGuideContent
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationCancelDialog
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationRefundPolicyDialog

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

                CancelReservationSideEffect.NavigateToCancelReservationConfirm -> {
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
        onAgreementChange = { isChecked ->
            viewModel.handleIntent(CancelReservationIntent.UpdateAgreement(isChecked))
        },
        onNextClick = {
            viewModel.handleIntent(CancelReservationIntent.OnNextClick)
        },
        onCancelDialogDismiss = {
            viewModel.handleIntent(CancelReservationIntent.DismissCancelDialog)
        },
        onCancelDialogConfirm = {
            viewModel.handleIntent(CancelReservationIntent.ConfirmCancel)
        },
        onInfoClick = {
            viewModel.handleIntent(CancelReservationIntent.ShowRefundPolicyDialog)
        },
        onRefundPolicyDismiss = {
            viewModel.handleIntent(CancelReservationIntent.DismissRefundPolicyTooltip)
        },
    )
}

@Composable
@Suppress("LongParameterList")
private fun CancelReservationScreenContent(
    state: CancelReservationState,
    pagerState: PagerState,
    onBackClick: () -> Unit,
    onReasonToggle: (CancelReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    onAgreementChange: (Boolean) -> Unit,
    onNextClick: () -> Unit,
    onCancelDialogDismiss: () -> Unit,
    onCancelDialogConfirm: () -> Unit,
    onInfoClick: () -> Unit,
    onRefundPolicyDismiss: () -> Unit,
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
        if (state.showCancelDialog) {
            ReservationCancelDialog(
                status = state.reservationStatus,
                refundCondition = state.refundCondition,
                onDismiss = onCancelDialogDismiss,
                onCancel = onCancelDialogDismiss,
                onConfirm = onCancelDialogConfirm,
                onInfoClick = onInfoClick,
            )
        }

        if (state.showRefundPolicyTooltip) {
            ReservationRefundPolicyDialog(
                onDismissRequest = onRefundPolicyDismiss,
            )
        }

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
                            shootingDateFormatted = state.shootingDateFormatted,
                            cancelDateFormatted = state.cancelDateFormatted,
                            totalPrice = state.totalPrice,
                            refundPrice = state.refundPrice,
                            cancellationFee = state.cancellationFee,
                            isAgreementChecked = state.isAgreementChecked,
                            onAgreementChange = { isChecked ->
                                onAgreementChange(isChecked)
                            },
                        )
                    }
                }
            }

            CommonBottomButton(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                text = stringResource(R.string.refund_button_next),
                onClick = onNextClick,
                enabled = state.isNextButtonEnabled(),
            )
        }
    }
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
        onAgreementChange = {},
        onNextClick = {},
        onCancelDialogDismiss = {},
        onCancelDialogConfirm = {},
        onInfoClick = {},
        onRefundPolicyDismiss = {},
    )
}

@Preview
@Composable
private fun CancelReservationScreenRefundGuidePreview() {
    CancelReservationScreenContent(
        state =
            CancelReservationState(
                orderId = "order123",
                selectedReasons = setOf(CancelReason.SCHEDULE),
                directInputText = "",
                currentPagerPage = CancelReservationPagerPage.REFUND_GUIDE,
                shootingDateFormatted = "25.01.09",
                cancelDateFormatted = "25.01.05",
            ),
        pagerState = rememberPagerState(initialPage = 1) { CancelReservationPagerPage.size },
        onBackClick = {},
        onReasonToggle = {},
        onDirectInputChange = {},
        onAgreementChange = {},
        onNextClick = {},
        onCancelDialogDismiss = {},
        onCancelDialogConfirm = {},
        onInfoClick = {},
        onRefundPolicyDismiss = {},
    )
}
