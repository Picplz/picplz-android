package com.hm.picplz.ui.model

enum class DeviceCategory {
    CAMERA,
    PHONE
}

sealed class Device {
    abstract val id: String
    abstract val companyName: String
    abstract val category: DeviceCategory

    data class PhoneDevice(
        override val id: String = "",
        override val companyName: String,
        val modelName: String? = null
    ) : Device() {
        override val category = DeviceCategory.PHONE
    }

    data class CameraDevice(
        override val id: String = "",
        override val companyName: String,
        val modelName: String? = null,
        val cameraType: String? = null
    ) : Device() {
        override val category = DeviceCategory.CAMERA
    }
}