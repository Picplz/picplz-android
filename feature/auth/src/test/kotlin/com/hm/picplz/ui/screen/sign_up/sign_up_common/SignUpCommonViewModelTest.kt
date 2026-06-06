package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.content.Context
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.model.UserType
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.model.UploadUrlResponseDto
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.CustomerService
import com.hm.picplz.data.service.MemberService
import com.hm.picplz.data.service.S3Service
import com.hm.picplz.domain.model.CustomerSignup
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.usecase.LoginWithKakaoUseCase
import com.hm.picplz.feature.auth.R
import com.hm.picplz.ui.screen.login.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpCommonViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `social profile image skips upload requirement and submits customer with null profileImage`() =
        runTest {
            val tokenManager =
                buildTokenManager(
                    socialCode = "SOCIAL_CODE",
                    socialEmail = "user@example.com",
                    socialProvider = "KAKAO",
                )

            val customerService =
                FakeCustomerService(
                    createCustomerResult = Result.success(Unit),
                )

            val viewModel =
                SignUpCommonViewModel(
                    customerService = customerService,
                    memberService = FakeMemberService(),
                    s3Service = FakeS3Service(),
                    tokenManager = tokenManager,
                    loginWithKakaoUseCase = buildLoginWithKakaoUseCase(),
                )

            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpCommonIntent.SetNickname("profileless"))
            viewModel.handleIntent(
                SignUpCommonIntent.SetProfileImageUri(
                    newProfileImageUri = "https://example.com/social-profile.jpg",
                    isUserSelected = false,
                ),
            )
            viewModel.handleIntent(SignUpCommonIntent.ClickUserTypeButton(UserType.User))
            viewModel.handleIntent(SignUpCommonIntent.NavigateToSelected)
            advanceUntilIdle()

            val navigateSideEffect =
                sideEffect.await() as SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected
            val signup = customerService.lastSignup ?: error("createCustomer signup should be submitted")

            assertEquals("sign-up-completion", navigateSideEffect.destination)
            assertEquals("profileless", navigateSideEffect.user.nickname)
            assertEquals("https://example.com/social-profile.jpg", navigateSideEffect.user.profileImageUri)
            assertEquals(null, navigateSideEffect.user.profileImageObjectKey)
            assertEquals(1, customerService.createCustomerCallCount)
            assertEquals(null, signup.profileImage)
        }

    @Test
    fun `missing social code blocks customer signup and shows social info missing toast`() =
        runTest {
            val tokenManager =
                buildTokenManager(
                    socialCode = null,
                    socialEmail = null,
                    socialProvider = null,
                )

            val customerService =
                FakeCustomerService(
                    createCustomerResult = Result.success(Unit),
                )

            val viewModel =
                SignUpCommonViewModel(
                    customerService = customerService,
                    memberService = FakeMemberService(),
                    s3Service = FakeS3Service(),
                    tokenManager = tokenManager,
                    loginWithKakaoUseCase = buildLoginWithKakaoUseCase(),
                )

            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpCommonIntent.SetNickname("nullsocial"))
            viewModel.handleIntent(SignUpCommonIntent.ClickUserTypeButton(UserType.User))
            viewModel.handleIntent(SignUpCommonIntent.NavigateToSelected)
            advanceUntilIdle()

            val showToast = sideEffect.await() as SignUpSideEffect.ShowToast
            val signup = customerService.lastSignup

            assertEquals(R.string.sign_up_social_info_missing, showToast.messageResId)
            assertEquals(null, signup)
            assertEquals(0, customerService.createCustomerCallCount)
            assertEquals(AppError.Auth.SocialInfoMissing, viewModel.state.value.error)
        }

    @Test
    fun `user selected profile image without object key blocks submission with upload failed toast`() =
        runTest {
            val tokenManager =
                buildTokenManager(
                    socialCode = "SOCIAL_CODE",
                    socialEmail = "user@example.com",
                    socialProvider = "KAKAO",
                )
            val customerService = FakeCustomerService(createCustomerResult = Result.success(Unit))

            val viewModel =
                SignUpCommonViewModel(
                    customerService = customerService,
                    memberService = FakeMemberService(),
                    s3Service = FakeS3Service(),
                    tokenManager = tokenManager,
                    loginWithKakaoUseCase = buildLoginWithKakaoUseCase(),
                )

            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpCommonIntent.SetNickname("profileless"))
            viewModel.handleIntent(
                SignUpCommonIntent.SetProfileImageUri(
                    newProfileImageUri = "file:///content/user-picked.jpg",
                    isUserSelected = true,
                ),
            )
            viewModel.handleIntent(SignUpCommonIntent.ClickUserTypeButton(UserType.User))
            viewModel.handleIntent(SignUpCommonIntent.NavigateToSelected)
            advanceUntilIdle()

            val toastEffect = sideEffect.await() as SignUpSideEffect.ShowToast

            assertEquals(R.string.sign_up_profile_image_upload_failed, toastEffect.messageResId)
            assertEquals(0, customerService.createCustomerCallCount)
        }

    @Test
    fun `complete signup re-logins with Kakao and emits signup completed when registered token is returned`() =
        runTest {
            val viewModel =
                SignUpCommonViewModel(
                    customerService = FakeCustomerService(createCustomerResult = Result.success(Unit)),
                    memberService = FakeMemberService(),
                    s3Service = FakeS3Service(),
                    tokenManager =
                        buildTokenManager(
                            socialCode = "SOCIAL_CODE",
                            socialEmail = "user@example.com",
                            socialProvider = "KAKAO",
                        ),
                    loginWithKakaoUseCase =
                        buildLoginWithKakaoUseCase(
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
                )

            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpCommonIntent.CompleteSignup(mock(Context::class.java)))
            advanceUntilIdle()

            assertEquals(SignUpSideEffect.SignupCompleted, sideEffect.await())
            assertEquals(false, viewModel.state.value.isSubmitting)
        }

    @Test
    fun `complete signup blocks main navigation when Kakao re-login returns unregistered response`() =
        runTest {
            val viewModel =
                SignUpCommonViewModel(
                    customerService = FakeCustomerService(createCustomerResult = Result.success(Unit)),
                    memberService = FakeMemberService(),
                    s3Service = FakeS3Service(),
                    tokenManager =
                        buildTokenManager(
                            socialCode = "SOCIAL_CODE",
                            socialEmail = "user@example.com",
                            socialProvider = "KAKAO",
                        ),
                    loginWithKakaoUseCase =
                        buildLoginWithKakaoUseCase(
                            loginResult =
                                Result.success(
                                    KaKaoLoginResponse(
                                        socialCode = "SOCIAL_CODE",
                                        socialEmail = "user@example.com",
                                        socialProvider = "KAKAO",
                                        accessToken = "ACCESS_TOKEN",
                                        refreshToken = "REFRESH_TOKEN",
                                        registered = false,
                                    ),
                                ),
                        ),
                )

            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpCommonIntent.CompleteSignup(mock(Context::class.java)))
            advanceUntilIdle()

            val showToast = sideEffect.await() as SignUpSideEffect.ShowToast

            assertEquals(R.string.sign_up_social_info_missing, showToast.messageResId)
            assertEquals(AppError.Auth.SocialInfoMissing, viewModel.state.value.error)
            assertEquals(false, viewModel.state.value.isSubmitting)
        }

    @Test
    fun `complete signup blocks main navigation when Kakao re-login returns no access token`() =
        runTest {
            val viewModel =
                SignUpCommonViewModel(
                    customerService = FakeCustomerService(createCustomerResult = Result.success(Unit)),
                    memberService = FakeMemberService(),
                    s3Service = FakeS3Service(),
                    tokenManager =
                        buildTokenManager(
                            socialCode = "SOCIAL_CODE",
                            socialEmail = "user@example.com",
                            socialProvider = "KAKAO",
                        ),
                    loginWithKakaoUseCase =
                        buildLoginWithKakaoUseCase(
                            loginResult =
                                Result.success(
                                    KaKaoLoginResponse(
                                        socialCode = "SOCIAL_CODE",
                                        socialEmail = "user@example.com",
                                        socialProvider = "KAKAO",
                                        accessToken = null,
                                        refreshToken = "REFRESH_TOKEN",
                                        registered = true,
                                    ),
                                ),
                        ),
                )

            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpCommonIntent.CompleteSignup(mock(Context::class.java)))
            advanceUntilIdle()

            val showToast = sideEffect.await() as SignUpSideEffect.ShowToast

            assertEquals(R.string.sign_up_social_info_missing, showToast.messageResId)
            assertEquals(AppError.Auth.SocialInfoMissing, viewModel.state.value.error)
            assertEquals(false, viewModel.state.value.isSubmitting)
        }

    private fun buildTokenManager(
        socialCode: String?,
        socialEmail: String?,
        socialProvider: String?,
    ): TokenManager {
        val tokenManager = mock(TokenManager::class.java)
        `when`(tokenManager.getSocialCode()).thenReturn(socialCode)
        `when`(tokenManager.getSocialEmail()).thenReturn(socialEmail)
        `when`(tokenManager.getSocialProvider()).thenReturn(socialProvider)
        return tokenManager
    }

    private fun buildLoginWithKakaoUseCase(
        loginResult: AppResult<KaKaoLoginResponse> =
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
    ) = LoginWithKakaoUseCase(FakeAuthRepository(loginResult))

    private class FakeCustomerService(
        private val createCustomerResult: AppResult<Unit>,
    ) : CustomerService {
        var createCustomerCallCount = 0
            private set
        var lastSignup: CustomerSignup? = null
            private set

        override suspend fun createCustomer(signup: CustomerSignup): AppResult<Unit> {
            createCustomerCallCount++
            lastSignup = signup
            return createCustomerResult
        }
    }

    private class FakeMemberService : MemberService {
        override suspend fun checkNicknameAvailable(nickname: String): AppResult<Boolean> = Result.success(true)

        override suspend fun getMemberInfo(memberId: Long): AppResult<MemberProfile> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): AppResult<Unit> {
            return Result.success(Unit)
        }
    }

    private class FakeS3Service : S3Service {
        override suspend fun getUploadUrl(
            imageType: String,
            filename: String,
        ): AppResult<UploadUrlResponseDto> {
            return Result.failure(AppError.Network.NoConnection)
        }

        override suspend fun uploadImage(
            uploadUrl: String,
            imageBytes: ByteArray,
            contentType: String,
        ): AppResult<Unit> {
            return Result.failure(AppError.Network.NoConnection)
        }
    }

    private class FakeAuthRepository(
        private val loginResult: AppResult<KaKaoLoginResponse>,
    ) : AuthRepository {
        override suspend fun loginWithKakao(context: Context): AppResult<KaKaoLoginResponse> = loginResult

        override suspend fun getKakaoUserInfo(): AppResult<KakaoUserInfo> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun unlinkKakao(): AppResult<Unit> {
            throw NotImplementedError("Not used in test")
        }

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean = true

        override fun getCurrentMemberId(): Long? = null
    }
}
