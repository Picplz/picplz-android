package com.hm.picplz.data.repository

import android.content.Context
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.AuthService
import com.hm.picplz.data.service.KakaoAuthService
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authService: AuthService,
        private val kakaoAuthService: KakaoAuthService,
        private val tokenManager: TokenManager,
    ) : AuthRepository {
        override suspend fun loginWithKakao(context: Context): AppResult<KaKaoLoginResponse> {
            return runCatchingAppError {
                val token = getKakaoOAuthToken(context).getOrThrow()
                val response = authService.loginWithKaKao(token.accessToken).getOrThrow()
                response
            }.onSuccess { response ->
                response.accessToken?.let { tokenManager.saveLoginToken(it, response.refreshToken) }
                tokenManager.saveSocialInfo(
                    socialCode = response.socialCode,
                    socialEmail = response.socialEmail,
                    socialProvider = response.socialProvider,
                )
            }
        }

        private suspend fun getKakaoOAuthToken(context: Context) =
            if (kakaoAuthService.isKakaoTalkLoginAvailable(context)) {
                kakaoAuthService.loginWithKakaoTalk(context)
            } else {
                kakaoAuthService.loginWithKakaoAccount(context)
            }

        override suspend fun getKakaoUserInfo(): AppResult<KakaoUserInfo> {
            return kakaoAuthService.getUserInfo()
        }

        override suspend fun unlinkKakao(): AppResult<Unit> {
            return kakaoAuthService.unlink()
        }

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean {
            return kakaoAuthService.isKakaoTalkLoginAvailable(context)
        }

        override fun getCurrentMemberId(): Long? = tokenManager.getMemberId()
    }
