package com.hm.picplz.data.repository

import android.content.Context
import com.hm.picplz.data.service.AuthService
import com.hm.picplz.data.service.KakaoAuthService
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val kakaoAuthService: KakaoAuthService
) : AuthRepository {

    override suspend fun loginWithKakao(context: Context): Result<KaKaoLoginResponse> {
        return getKakaoOAuthToken(context)
            .mapCatching { token -> authService.loginWithKaKao(token.accessToken).getOrThrow() }
    }

    private suspend fun getKakaoOAuthToken(context: Context) =
        if (kakaoAuthService.isKakaoTalkLoginAvailable(context)) {
            kakaoAuthService.loginWithKakaoTalk(context)
        } else {
            kakaoAuthService.loginWithKakaoAccount(context)
        }

    override suspend fun getKakaoUserInfo(): Result<KakaoUserInfo> {
        return kakaoAuthService.getUserInfo()
    }

    override suspend fun unlinkKakao(): Result<Unit> {
        return kakaoAuthService.unlink()
    }

    override fun isKakaoTalkLoginAvailable(context: Context): Boolean {
        return kakaoAuthService.isKakaoTalkLoginAvailable(context)
    }
}
