package com.hm.picplz.data.service

import android.content.Context
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.KakaoUserInfo
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface KakaoAuthService {
    suspend fun loginWithKakaoTalk(context: Context): AppResult<OAuthToken>

    suspend fun loginWithKakaoAccount(context: Context): AppResult<OAuthToken>

    fun isKakaoTalkLoginAvailable(context: Context): Boolean

    suspend fun getUserInfo(): AppResult<KakaoUserInfo>

    suspend fun unlink(): AppResult<Unit>
}

class KakaoAuthServiceImpl
    @Inject
    constructor() : KakaoAuthService {
        override suspend fun loginWithKakaoTalk(context: Context): AppResult<OAuthToken> =
            suspendCancellableCoroutine { cont ->
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        cont.resume(Result.failure(AppError.Auth.KakaoSdk(error)))
                    } else if (token != null) {
                        cont.resume(Result.success(token))
                    } else {
                        cont.resume(Result.failure(AppError.Auth.KakaoTokenMissing))
                    }
                }
            }

        override suspend fun loginWithKakaoAccount(context: Context): AppResult<OAuthToken> =
            suspendCancellableCoroutine { cont ->
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        cont.resume(Result.failure(AppError.Auth.KakaoSdk(error)))
                    } else if (token != null) {
                        cont.resume(Result.success(token))
                    } else {
                        cont.resume(Result.failure(AppError.Auth.KakaoTokenMissing))
                    }
                }
            }

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean =
            UserApiClient.instance.isKakaoTalkLoginAvailable(context)

        override suspend fun getUserInfo(): AppResult<KakaoUserInfo> =
            suspendCancellableCoroutine { cont ->
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        cont.resume(Result.failure(AppError.Auth.KakaoSdk(error)))
                    } else {
                        val profileImageUrl = user?.kakaoAccount?.profile?.profileImageUrl
                        cont.resume(Result.success(KakaoUserInfo(profileImageUrl = profileImageUrl)))
                    }
                }
            }

        override suspend fun unlink(): AppResult<Unit> =
            suspendCancellableCoroutine { cont ->
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        cont.resume(Result.failure(AppError.Auth.KakaoSdk(error)))
                    } else {
                        cont.resume(Result.success(Unit))
                    }
                }
            }
    }
