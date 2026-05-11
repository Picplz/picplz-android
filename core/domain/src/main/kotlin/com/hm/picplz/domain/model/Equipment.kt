package com.hm.picplz.domain.model

data class Equipment(
    val id: String,
    val type: String,
    val deviceName: String,
    val isEnabled: Boolean = true,
)
