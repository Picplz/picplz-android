package com.hm.picplz.ui.screen.my_page

sealed interface OrderSheetSideEffect {
    data object NavigateBack : OrderSheetSideEffect

    data object ShowReceiptUnavailable : OrderSheetSideEffect
}
