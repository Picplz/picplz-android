package com.hm.picplz.data.service

import com.hm.picplz.data.model.KaKaoLoginResponse
import com.hm.picplz.data.model.KakaoUserInfo
import com.hm.picplz.data.source.AuthSource
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface AuthService {
    suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse>
    suspend fun getKakaoUserInfo(): Result<KakaoUserInfo>
}

class AuthServiceImpl @Inject constructor(
    private val authSource: AuthSource
) : AuthService {
    override suspend fun loginWithKaKao(accessToken: String): Result<KaKaoLoginResponse> {
        return authSource.loginWithKaKao(accessToken = accessToken)
    }

    override suspend fun getKakaoUserInfo(): Result<KakaoUserInfo> =
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
}
