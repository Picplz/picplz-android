package com.hm.picplz.ui.screen.my_page

sealed interface OrderSheetIntent {
    data object NavigateBack : OrderSheetIntent

    data object ViewReceipt : OrderSheetIntent
}
