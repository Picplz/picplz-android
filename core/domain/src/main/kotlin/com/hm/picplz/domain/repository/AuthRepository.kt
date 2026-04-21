package com.hm.picplz.domain.repository

import android.content.Context
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo

interface AuthRepository {
    suspend fun loginWithKakao(context: Context): Result<KaKaoLoginResponse>

    suspend fun getKakaoUserInfo(): Result<KakaoUserInfo>

    suspend fun unlinkKakao(): Result<Unit>

    fun isKakaoTalkLoginAvailable(context: Context): Boolean

    fun getCurrentMemberId(): Long?
}
