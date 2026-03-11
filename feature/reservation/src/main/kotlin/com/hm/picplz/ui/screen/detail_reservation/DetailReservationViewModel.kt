package com.hm.picplz.ui.screen.detail_reservation

import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetailReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DetailReservationState())
    val state: StateFlow<DetailReservationState> get() = _state

    init {
        // TODO: API에서 촬영 일시 데이터를 받아와야 합니다.
        // 임시로 더미 데이터 설정 (촬영일: 4일 후)
        val dummyShootingDateTime = LocalDateTime.now().plusDays(4)
        val dummyConfirmedDateTime = LocalDateTime.now().minusHours(12)

        _state.update {
            it.copy(
                shootingDateTime = dummyShootingDateTime,
                confirmedDateTime = dummyConfirmedDateTime,
            )
        }
    }

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
                val currentState = _state.value
                val refundCondition =
                    RefundCondition.calculate(
                        currentDateTime = LocalDateTime.now(),
                        shootingDateTime = currentState.shootingDateTime,
                        confirmedDateTime = currentState.confirmedDateTime,
                    )

                _state.update {
                    it.copy(
                        showCancelDialog = true,
                        refundCondition = refundCondition,
                    )
                }
            }

            is DetailReservationIntent.DismissCancelDialog -> {
                _state.update { it.copy(showCancelDialog = false) }
            }

            is DetailReservationIntent.ConfirmCancel -> {
                _state.update { it.copy(showCancelDialog = false) }
                // TODO: 예약 취소 API 호출
            }

            is DetailReservationIntent.ShowRefundPolicyDialog -> {
                _state.update {
                    it.copy(
                        showRefundPolicyTooltip = true,
                        showCancelDialog = false,
                    )
                }
            }

            is DetailReservationIntent.DismissRefundPolicyTooltip -> {
                _state.update {
                    it.copy(
                        showRefundPolicyTooltip = false,
                        showCancelDialog = true,
                    )
                }
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
