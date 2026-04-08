package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.core.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderSheetViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(DEV_MOCK_STATE)
        val state: StateFlow<OrderSheetState> get() = _state

        private val _sideEffect = Channel<OrderSheetSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: OrderSheetIntent) {
            when (intent) {
                is OrderSheetIntent.NavigateBack -> {
                    sendSideEffect(OrderSheetSideEffect.NavigateBack)
                }
                is OrderSheetIntent.ViewReceipt -> {
                    sendSideEffect(OrderSheetSideEffect.ShowReceiptUnavailable)
                }
            }
        }

        private fun sendSideEffect(effect: OrderSheetSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }

        companion object {
            private val DEV_MOCK_STATE =
                OrderSheetState(
                    orderInfo =
                        OrderInfo(
                            orderNum = "nnnnmmmmdd123456",
                            orderDate = "2025-03-09 19:09:14",
                            clientName = "주은강",
                            clientPhoneNumber = "01023293185",
                            photographerName = "주로그",
                            productName = "남친생기는 프사❤️",
                            productCost = 12000,
                            productDescription =
                                "작가가 해놓은 한줄소개 길어지면 이렇게 처리해주세요ㅇ모ㅓㅏ로망리ㅗㅁ어라ㅣ멍;ㅣㄹ",
                            discountCost = 0,
                            totalCost = 12000,
                            shootingTime = "25.01.09 - 오후 02:00",
                            shootingLocation = "서울특별시 종로구 효자로 33, 네번째 테이블 창문 앞",
                            shootingLatitude = 37.5788,
                            shootingLongitude = 126.9748,
                            productImage = R.drawable.ic_launcher_background,
                            cardName = "신한카드(************5189)",
                            paymentDate = "2025.03.09 19:09:14",
                        ),
                )
        }
    }
