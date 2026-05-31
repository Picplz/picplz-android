package com.hm.picplz.data.source

import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.AuthApi
import com.hm.picplz.data.model.KaKaoLoginRequest
import com.hm.picplz.data.util.toHttpAppError
import com.hm.picplz.domain.model.KaKaoLoginResponse
import javax.inject.Inject

interface AuthSource {
    suspend fun loginWithKaKao(accessToken: String): AppResult<KaKaoLoginResponse>
}

class AuthSourceImpl
    @Inject
    constructor(
        private val authApi: AuthApi,
    ) : AuthSource {
        override suspend fun loginWithKaKao(accessToken: String): AppResult<KaKaoLoginResponse> {
            return runCatchingAppError {
                val response = authApi.loginWithKaKao(KaKaoLoginRequest(accessToken))
                if (response.isSuccessful) {
                    response.body()?.toDomain()
                        ?: throw AppError.Network.EmptyBody
                } else {
                    throw response.toHttpAppError()
                }
            }
        }
    }
