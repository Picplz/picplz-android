package com.hm.picplz.ui.model

enum class DeviceCategory {
    CAMERA,
    PHONE
}

data class Device(
    val id: String = "",
    val companyName: String,
    val productName: String,
    val category: DeviceCategory
) {
    val fullName: String
        get() = "$companyName $productName"
}