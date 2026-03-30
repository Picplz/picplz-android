package com.hm.picplz.ui.screen.order_detail

data class OrderDetailState(
    val orderNumber: String = "",
    val orderTime: String = "",
    val customerName: String = "",
    val phoneNumber: String = "",
    val photographerName: String = "",
    val productName: String = "",
    val productImageUrl: String? = null,
    val price: Int = 0,
    val productDescription: String = "",
    val shootingTime: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    companion object {
        fun idle(): OrderDetailState = OrderDetailState()
    }
}
