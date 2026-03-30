package com.hm.picplz.ui.screen.order_detail

sealed interface OrderDetailIntent {
    data object OnBackClick : OrderDetailIntent

    data object OnNextClick : OrderDetailIntent
}
