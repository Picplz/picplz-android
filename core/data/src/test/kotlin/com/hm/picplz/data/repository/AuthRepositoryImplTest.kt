package com.hm.picplz.data.repository

import android.content.Context
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.AuthService
import com.hm.picplz.data.service.KakaoAuthService
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.nullable
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`

class AuthRepositoryImplTest {
    @Test
    fun `loginWithKakao saves login token and social info when backend returns access token`() =
        runTest {
            val tokenManager = mock(TokenManager::class.java)
            val authService =
                FakeAuthService(
                    loginResult =
                        Result.success(
                            KaKaoLoginResponse(
                                socialCode = "SOCIAL_CODE",
                                socialEmail = "user@example.com",
                                socialProvider = "KAKAO",
                                accessToken = "ACCESS_TOKEN",
                                refreshToken = "REFRESH_TOKEN",
                                registered = true,
                            ),
                        ),
                )
            val repository =
                AuthRepositoryImpl(
                    authService = authService,
                    kakaoAuthService = FakeKakaoAuthService(),
                    tokenManager = tokenManager,
                )

            val response = repository.loginWithKakao(mock(Context::class.java)).getOrThrow()

            assertEquals("KAKAO_OAUTH_TOKEN", authService.lastAccessToken)
            assertEquals("ACCESS_TOKEN", response.accessToken)
            verify(tokenManager).saveLoginToken("ACCESS_TOKEN", "REFRESH_TOKEN")
            verify(tokenManager).saveSocialInfo(
                socialCode = "SOCIAL_CODE",
                socialEmail = "user@example.com",
                socialProvider = "KAKAO",
            )
        }

    @Test
    fun `loginWithKakao saves social info but not login token when backend access token is missing`() =
        runTest {
            val tokenManager = mock(TokenManager::class.java)
            val repository =
                AuthRepositoryImpl(
                    authService =
                        FakeAuthService(
                            loginResult =
                                Result.success(
                                    KaKaoLoginResponse(
                                        socialCode = "SOCIAL_CODE",
                                        socialEmail = "user@example.com",
                                        socialProvider = "KAKAO",
                                        accessToken = null,
                                        refreshToken = "REFRESH_TOKEN",
                                        registered = false,
                                    ),
                                ),
                        ),
                    kakaoAuthService = FakeKakaoAuthService(),
                    tokenManager = tokenManager,
                )

            val response = repository.loginWithKakao(mock(Context::class.java)).getOrThrow()

            assertEquals(null, response.accessToken)
            verify(tokenManager, never()).saveLoginToken(anyString(), nullable(String::class.java))
            verify(tokenManager).saveSocialInfo(
                socialCode = "SOCIAL_CODE",
                socialEmail = "user@example.com",
                socialProvider = "KAKAO",
            )
        }

    @Test
    fun `loginWithKakao does not save token or social info when Kakao OAuth fails`() =
        runTest {
            val tokenManager = mock(TokenManager::class.java)
            val repository =
                AuthRepositoryImpl(
                    authService =
                        FakeAuthService(
                            loginResult =
                                Result.success(
                                    KaKaoLoginResponse(
                                        socialCode = "SOCIAL_CODE",
                                        socialEmail = "user@example.com",
                                        socialProvider = "KAKAO",
                                        accessToken = "ACCESS_TOKEN",
                                        refreshToken = "REFRESH_TOKEN",
                                        registered = true,
                                    ),
                                ),
                        ),
                    kakaoAuthService =
                        FakeKakaoAuthService(
                            loginResult = Result.failure(AppError.Auth.KakaoTokenMissing),
                        ),
                    tokenManager = tokenManager,
                )

            val error = repository.loginWithKakao(mock(Context::class.java)).exceptionOrNull()

            assertTrue(error is AppError.Auth.KakaoTokenMissing)
            verifyNoInteractions(tokenManager)
        }

    private class FakeAuthService(
        private val loginResult: AppResult<KaKaoLoginResponse>,
    ) : AuthService {
        var lastAccessToken: String? = null
            private set

        override suspend fun loginWithKaKao(accessToken: String): AppResult<KaKaoLoginResponse> {
            lastAccessToken = accessToken
            return loginResult
        }
    }

    private class FakeKakaoAuthService(
        private val loginResult: AppResult<OAuthToken> = Result.success(buildOAuthToken()),
        private val kakaoTalkAvailable: Boolean = false,
    ) : KakaoAuthService {
        override suspend fun loginWithKakaoTalk(context: Context): AppResult<OAuthToken> = loginResult

        override suspend fun loginWithKakaoAccount(context: Context): AppResult<OAuthToken> = loginResult

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean = kakaoTalkAvailable

        override suspend fun getUserInfo(): AppResult<KakaoUserInfo> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun unlink(): AppResult<Unit> {
            throw NotImplementedError("Not used in test")
        }
    }
}

private fun buildOAuthToken(): OAuthToken {
    val token = mock(OAuthToken::class.java)
    `when`(token.accessToken).thenReturn("KAKAO_OAUTH_TOKEN")
    return token
}
