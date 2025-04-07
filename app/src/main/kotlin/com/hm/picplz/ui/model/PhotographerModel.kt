package com.hm.picplz.ui.model

data class Equipment(
    val id: Int,
    val type: String,
    val deviceName: String,
    val isEnabled: Boolean = true
)