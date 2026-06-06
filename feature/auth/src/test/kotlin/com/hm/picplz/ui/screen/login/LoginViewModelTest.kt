package com.hm.picplz.ui.screen.login

import android.app.Application
import android.content.Context
import android.util.Log
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.usecase.GetKakaoUserInfoUseCase
import com.hm.picplz.domain.usecase.LoginWithKakaoUseCase
import com.hm.picplz.domain.usecase.UnlinkKakaoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockedLog: MockedStatic<Log>

    @Before
    fun setUp() {
        mockedLog = Mockito.mockStatic(Log::class.java)
        mockedLog.`when`<Int> { Log.d(anyString(), anyString()) }.thenReturn(0)
        mockedLog.`when`<Int> { Log.i(anyString(), anyString()) }.thenReturn(0)
        mockedLog
            .`when`<Int> { Log.e(anyString(), anyString(), any(Throwable::class.java)) }
            .thenReturn(0)
    }

    @After
    fun tearDown() {
        mockedLog.close()
    }

    @Test
    fun `registered kakao login emits LoginSuccess and clears loading`() =
        runTest {
            val authRepository =
                FakeAuthRepository(
                    loginResult =
                        AppResult.success(
                            KaKaoLoginResponse(
                                socialCode = "SOCIAL_CODE",
                                socialEmail = "test@example.com",
                                socialProvider = "KAKAO",
                                accessToken = "access-token",
                                refreshToken = "refresh-token",
                                registered = true,
                            ),
                        ),
                    userInfoResult = AppResult.failure(IOException("not used")),
                )
            val viewModel = createViewModel(authRepository)

            val sideEffect = async { viewModel.sideEffect.first() }
            viewModel.handleIntent(LoginIntent.StartKakaoLogin(createContext()))
            advanceUntilIdle()

            assertEquals(LoginSideEffect.LoginSuccess, sideEffect.await())
            assertFalse(viewModel.state.value.isLoading)
            assertEquals(1, authRepository.loginWithKakaoCallCount)
            assertEquals(0, authRepository.getKakaoUserInfoCallCount)
            assertTrue(viewModel.state.value.error == null)
        }

    @Test
    fun `unregistered kakao login fetches profile and navigates to sign up`() =
        runTest {
            val authRepository =
                FakeAuthRepository(
                    loginResult =
                        AppResult.success(
                            KaKaoLoginResponse(
                                socialCode = "SOCIAL_CODE",
                                socialEmail = "test@example.com",
                                socialProvider = "KAKAO",
                                accessToken = "access-token",
                                refreshToken = "refresh-token",
                                registered = false,
                            ),
                        ),
                    userInfoResult =
                        AppResult.success(
                            KakaoUserInfo(
                                profileImageUrl = "https://example.com/profile.jpg",
                            ),
                        ),
                )
            val viewModel = createViewModel(authRepository)
            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(LoginIntent.StartKakaoLogin(createContext()))
            advanceUntilIdle()

            assertEquals(
                LoginSideEffect.NavigateToSignUp("https://example.com/profile.jpg"),
                sideEffect.await(),
            )
            assertFalse(viewModel.state.value.isLoading)
            assertEquals(1, authRepository.loginWithKakaoCallCount)
            assertEquals(1, authRepository.getKakaoUserInfoCallCount)
            assertEquals(null, viewModel.state.value.error)
        }

    @Test
    fun `unregistered kakao login without user profile clears loading and emits LoginFailed with error`() =
        runTest {
            val userInfoError = IOException("kakao api network error")
            val authRepository =
                FakeAuthRepository(
                    loginResult =
                        AppResult.success(
                            KaKaoLoginResponse(
                                socialCode = "SOCIAL_CODE",
                                socialEmail = "test@example.com",
                                socialProvider = "KAKAO",
                                accessToken = "access-token",
                                refreshToken = "refresh-token",
                                registered = false,
                            ),
                        ),
                    userInfoResult = AppResult.failure(userInfoError),
                )
            val viewModel = createViewModel(authRepository)
            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(LoginIntent.StartKakaoLogin(createContext()))
            advanceUntilIdle()

            val failedSideEffect = sideEffect.await() as LoginSideEffect.LoginFailed
            assertEquals(AppError.Network.NoConnection, failedSideEffect.error)
            assertFalse(viewModel.state.value.isLoading)
            assertEquals(AppError.Network.NoConnection, viewModel.state.value.error)
            assertEquals(1, authRepository.loginWithKakaoCallCount)
            assertEquals(1, authRepository.getKakaoUserInfoCallCount)
        }

    private fun createViewModel(authRepository: FakeAuthRepository): LoginViewModel {
        return LoginViewModel(
            loginWithKakaoUseCase = LoginWithKakaoUseCase(authRepository),
            getKakaoUserInfoUseCase = GetKakaoUserInfoUseCase(authRepository),
            unlinkKakaoUseCase = UnlinkKakaoUseCase(authRepository),
        )
    }

    private fun createContext(): Context {
        return Application()
    }

    private class FakeAuthRepository(
        private val loginResult: AppResult<KaKaoLoginResponse>,
        private val userInfoResult: AppResult<KakaoUserInfo>,
    ) : AuthRepository {
        var loginWithKakaoCallCount = 0
            private set

        var getKakaoUserInfoCallCount = 0
            private set

        override suspend fun loginWithKakao(context: Context): AppResult<KaKaoLoginResponse> {
            loginWithKakaoCallCount++
            return loginResult
        }

        override suspend fun getKakaoUserInfo(): AppResult<KakaoUserInfo> {
            getKakaoUserInfoCallCount++
            return userInfoResult
        }

        override suspend fun unlinkKakao(): AppResult<Unit> {
            return Result.success(Unit)
        }

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean {
            return true
        }

        override fun getCurrentMemberId(): Long? {
            return null
        }
    }
}
