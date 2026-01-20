package com.hm.picplz.data.api

import com.hm.picplz.data.model.KaKaoLoginRequest
import com.hm.picplz.data.model.KaKaoLoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/kakao")
    suspend fun loginWithKaKao(
        @Body request: KaKaoLoginRequest
    ): Response<KaKaoLoginResponseDto>
}