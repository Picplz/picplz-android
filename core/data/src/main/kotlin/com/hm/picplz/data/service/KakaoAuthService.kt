package com.hm.picplz.data.service

import android.content.Context
import com.hm.picplz.domain.model.KakaoUserInfo
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface KakaoAuthService {
    suspend fun loginWithKakaoTalk(context: Context): Result<OAuthToken>
    suspend fun loginWithKakaoAccount(context: Context): Result<OAuthToken>
    fun isKakaoTalkLoginAvailable(context: Context): Boolean
    suspend fun getUserInfo(): Result<KakaoUserInfo>
    suspend fun unlink(): Result<Unit>
}

class KakaoAuthServiceImpl @Inject constructor() : KakaoAuthService {

    override suspend fun loginWithKakaoTalk(context: Context): Result<OAuthToken> =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    cont.resume(Result.failure(error))
                } else if (token != null) {
                    cont.resume(Result.success(token))
                } else {
                    cont.resume(Result.failure(IllegalStateException("Token is null")))
                }
            }
        }

    override suspend fun loginWithKakaoAccount(context: Context): Result<OAuthToken> =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    cont.resume(Result.failure(error))
                } else if (token != null) {
                    cont.resume(Result.success(token))
                } else {
                    cont.resume(Result.failure(IllegalStateException("Token is null")))
                }
            }
        }

    override fun isKakaoTalkLoginAvailable(context: Context): Boolean =
        UserApiClient.instance.isKakaoTalkLoginAvailable(context)

    override suspend fun getUserInfo(): Result<KakaoUserInfo> =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    cont.resume(Result.failure(error))
                } else {
                    val profileImageUrl = user?.kakaoAccount?.profile?.profileImageUrl
                    cont.resume(Result.success(KakaoUserInfo(profileImageUrl = profileImageUrl)))
                }
            }
        }

    override suspend fun unlink(): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    cont.resume(Result.failure(error))
                } else {
                    cont.resume(Result.success(Unit))
                }
            }
        }
}
