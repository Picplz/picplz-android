package com.hm.picplz.domain.model

data class KaKaoLoginResponse(
    val socialCode: String?,
    val socialEmail: String?,
    val socialProvider: String,
    val accessToken: String?,
    val refreshToken: String?,
    val registered: Boolean,
)

data class KakaoUserInfo(
    val profileImageUrl: String?,
)
