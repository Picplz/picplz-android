package com.hm.picplz.ui.screen.detail_reservation

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DetailReservationState())
    val state: StateFlow<DetailReservationState> get() = _state

    fun handelIntent(intent: DetailReservationIntent) {
        when (intent) {
            // 상태 변경 확인 테스트를 위한 코드입니다.
            is DetailReservationIntent.NavigateToChat,
            is DetailReservationIntent.NavigateToHistory,
            is DetailReservationIntent.ConfirmReservation,
            -> {
                _state.update { it.copy(reservationStatus = it.reservationStatus.next()) }
            }

            is DetailReservationIntent.ShowCancelDialog -> {
                _state.update { it.copy(showCancelDialog = true) }
            }

            is DetailReservationIntent.DismissCancelDialog -> {
                _state.update { it.copy(showCancelDialog = false) }
            }

            is DetailReservationIntent.ConfirmCancel -> {
                _state.update { it.copy(showCancelDialog = false) }
                // TODO: 예약 취소 API 호출
            }
        }
    }

    // 상태 변경 확인 테스트를 위한 코드입니다.
    private fun ReservationStatus.next(): ReservationStatus =
        when (this) {
            ReservationStatus.WAITING_APPROVAL -> ReservationStatus.WAITING_PAYMENT
            ReservationStatus.WAITING_PAYMENT -> ReservationStatus.RESERVED
            ReservationStatus.RESERVED -> ReservationStatus.COMPLETED
            ReservationStatus.COMPLETED -> ReservationStatus.COMPLETED
        }
}
