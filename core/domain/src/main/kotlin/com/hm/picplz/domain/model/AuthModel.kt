package com.hm.picplz.domain.model

data class KaKaoLoginResponse(
    val socialEmail: String?,
    val socialProvider: String,
    val token: String?,
    val registered: Boolean,
)

data class KakaoUserInfo(
    val profileImageUrl: String?,
)
