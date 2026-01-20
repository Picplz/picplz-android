package com.hm.picplz.data.source

import com.hm.picplz.data.api.AuthApi
import com.hm.picplz.data.model.KaKaoLoginRequest
import com.hm.picplz.domain.model.KaKaoLoginResponse
import javax.inject.Inject

interface AuthSource {
    suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse>
}

class AuthSourceImpl
    @Inject
    constructor(
        private val authApi: AuthApi,
    ) : AuthSource {
        override suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse> {
            return runCatching {
                val response = authApi.loginWithKaKao(KaKaoLoginRequest(accessToken))
                if (response.isSuccessful) {
                    response.body()?.toDomain() ?: throw Exception("Response body is null")
                } else {
                    throw Exception("Login failed: ${response.code()} ${response.errorBody()?.string()}")
                }
            }
        }
    }
