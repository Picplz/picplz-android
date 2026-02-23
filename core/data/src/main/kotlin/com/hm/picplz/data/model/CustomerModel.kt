package com.hm.picplz.data.model

data class CreateCustomerRequest(
    val nickname: String,
    val socialEmail: String?,
    val socialProvider: String?,
    val socialCode: String?,
    val profileImage: String?,
)
