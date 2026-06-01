package com.hm.picplz.domain.repository

import android.content.Context
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo

interface AuthRepository {
    suspend fun loginWithKakao(context: Context): AppResult<KaKaoLoginResponse>

    suspend fun getKakaoUserInfo(): AppResult<KakaoUserInfo>

    suspend fun unlinkKakao(): AppResult<Unit>

    fun isKakaoTalkLoginAvailable(context: Context): Boolean

    fun getCurrentMemberId(): Long?
}
