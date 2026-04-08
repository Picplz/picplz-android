package com.hm.picplz.ui.screen.my_page

data class OrderSheetState(
    val orderInfo: OrderInfo = OrderInfo.EMPTY,
) {
    companion object {
        fun idle() = OrderSheetState()
    }
}

data class OrderInfo(
    val orderNum: String,
    val orderDate: String,
    val clientName: String,
    val clientPhoneNumber: String,
    val photographerName: String,
    val productName: String,
    val productCost: Int,
    val productDescription: String,
    val discountCost: Int,
    val totalCost: Int,
    val shootingTime: String,
    val shootingLocation: String,
    val shootingLatitude: Double,
    val shootingLongitude: Double,
    val productImage: Int,
    val cardName: String,
    val paymentDate: String,
) {
    companion object {
        val EMPTY =
            OrderInfo(
                orderNum = "",
                orderDate = "",
                clientName = "",
                clientPhoneNumber = "",
                photographerName = "",
                productName = "",
                productCost = 0,
                productDescription = "",
                discountCost = 0,
                totalCost = 0,
                shootingTime = "",
                shootingLocation = "",
                shootingLatitude = 0.0,
                shootingLongitude = 0.0,
                productImage = 0,
                cardName = "",
                paymentDate = "",
            )
    }
}
