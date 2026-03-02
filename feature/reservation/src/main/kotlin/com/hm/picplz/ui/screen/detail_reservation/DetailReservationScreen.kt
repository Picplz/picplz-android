package com.hm.picplz.ui.screen.detail_reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.ui.screen.detail_reservation.composable.DetailReservationBottomButtons
import com.hm.picplz.ui.screen.detail_reservation.composable.DetailReservationMap
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationInfoSection
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationProgressStepper
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationStatusHeader
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun DetailReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailReservationScreen(
        modifier = modifier,
        state = state,
        onChatClick = {
            viewModel.handelIntent(DetailReservationIntent.NavigateToChat)
        },
        onHistoryClick = {
            viewModel.handelIntent(DetailReservationIntent.NavigateToHistory)
        },
        onConfirmClick = {
            viewModel.handelIntent(DetailReservationIntent.ConfirmReservation)
        },
    )
}

@Composable
private fun DetailReservationScreen(
    state: DetailReservationState,
    onChatClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            DetailReservationMap(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                onCloseClick = {},
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                item {
                    ReservationStatusHeader(
                        modifier = Modifier.padding(vertical = 20.dp),
                        currentReservationStatus = state.reservationStatus,
                        onCancelClick = {},
                    )
                }

                item {
                    ReservationProgressStepper(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        currentReservationStep = state.reservationStatus.step,
                    )
                }

                item {
                    ReservationInfoSection(modifier = Modifier.padding(top = 28.dp, bottom = 24.dp))
                }
            }

            DetailReservationBottomButtons(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                currentReservationStatus = state.reservationStatus,
                onChatClick = onChatClick,
                onHistoryClick = onHistoryClick,
                onConfirmClick = onConfirmClick,
            )
        }
    }
}

@Preview
@Composable
fun DetailReservationScreenPreview() {
    DetailReservationScreen(
        state = DetailReservationState(),
        onChatClick = {},
        onHistoryClick = {},
        onConfirmClick = {},
    )
}
