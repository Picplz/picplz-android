package com.hm.picplz.domain.model

data class CustomerSignup(
    val nickname: String,
    val socialEmail: String?,
    val socialProvider: String?,
    val socialCode: String,
    val profileImage: String?,
)
