package com.hm.picplz.data.source

import com.hm.picplz.data.api.AuthApi
import com.hm.picplz.data.model.KaKaoLoginRequest
import com.hm.picplz.data.model.KaKaoLoginResponse
import javax.inject.Inject


interface AuthSource {
    suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse>
}

class AuthSourceImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthSource {
    override suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse> {
        return runCatching {
            val response = authApi.loginWithKaKao(KaKaoLoginRequest(accessToken))
            if (response.isSuccessful) {
                response.body() ?: throw Exception("응답 body가 null입니다.")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.errorBody()?.string()}")
            }
        }
    }
}