package com.hm.picplz.ui.screen.order_detail

sealed interface OrderDetailSideEffect {
    data object NavigateBack : OrderDetailSideEffect

    data object NavigateToNextStep : OrderDetailSideEffect
}
