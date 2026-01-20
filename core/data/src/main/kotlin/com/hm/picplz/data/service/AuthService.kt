package com.hm.picplz.data.service

import com.hm.picplz.data.source.AuthSource
import com.hm.picplz.domain.model.KaKaoLoginResponse
import javax.inject.Inject

interface AuthService {
    suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse>
}

class AuthServiceImpl @Inject constructor(
    private val authSource: AuthSource
) : AuthService {
    override suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse> {
        return authSource.loginWithKaKao(accessToken = accessToken)
    }
}
