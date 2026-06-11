package com.hm.picplz.ui.screen.my_page

import android.content.Context
import com.hm.picplz.common.model.UserType
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.model.UpdateMemberProfileCommand
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.repository.MemberRepository
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetMemberProfileUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads current member profile when member id exists`() =
        runTest {
            val viewModel =
                createViewModel(
                    currentMemberId = 1,
                    memberProfile =
                        MemberProfile(
                            id = 1,
                            nickname = "고객회원",
                            profileImage = "profile-image",
                            introduction = null,
                            instagram = null,
                            userType = UserType.User,
                        ),
                )

            advanceUntilIdle()

            assertEquals("고객회원", viewModel.state.value.nickname)
            assertEquals("profile-image", viewModel.state.value.profileImageUri)
            assertFalse(viewModel.state.value.hasPhotographerRole)
        }

    @Test
    fun `navigate to followed photographers emits navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToFollowedPhotographers)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToFollowedPhotographers,
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `navigate to photographer modify profile emits dedicated navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerModifyProfile)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToPhotographerModifyProfile,
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `navigate to package edit emits dedicated navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.applyDevPhotographerPreview()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPackageEdit)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToPackageEdit(1),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `navigate to photographer keyword edit emits dedicated navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.applyDevPhotographerPreview()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerKeywordEdit)
            advanceUntilIdle()

            val sideEffect = sideEffectDeferred.await()
            assertTrue(sideEffect is MyPageSideEffect.NavigateToPhotographerKeywordEdit)
            assertEquals(1, (sideEffect as MyPageSideEffect.NavigateToPhotographerKeywordEdit).photographerId)
        }

    @Test
    fun `navigate to photographer region edit emits dedicated navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.applyDevPhotographerPreview()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerRegionEdit)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToPhotographerActiveAreaEdit(1),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `photographer preview without package emits requires package toast`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.applyDevPhotographerPreview()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerPreview)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.ShowToast(com.hm.picplz.feature.mypage.R.string.my_page_preview_requires_package),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `photographer preview with package emits detail navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(
                MyPageIntent.ApplyDevPhotographerPreview(
                    hasShootings = false,
                    hasPackagePreview = true,
                    hasPortfolioPreview = false,
                ),
            )
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerPreview)
            advanceUntilIdle()

            val sideEffect = sideEffectDeferred.await()
            assertTrue(sideEffect is MyPageSideEffect.NavigateToPhotographerPreview)
            assertEquals(1, (sideEffect as MyPageSideEffect.NavigateToPhotographerPreview).photographerId)
        }

    @Test
    fun `apply photographer keyword summary updates profile summary`() {
        val viewModel = createViewModel()

        viewModel.handleIntent(MyPageIntent.ApplyPhotographerKeywordSummary("#캐주얼, #심플"))

        assertEquals("#캐주얼, #심플", viewModel.state.value.photographerProfile.keywordSummary)
    }

    @Test
    fun `photographer preview availability requires package and photographer id`() {
        assertFalse(PhotographerProfile(hasPackages = false, photographerId = 1).canPreviewProfile)
        assertFalse(PhotographerProfile(hasPackages = true, photographerId = 0).canPreviewProfile)
        assertTrue(PhotographerProfile(hasPackages = true, photographerId = 1).canPreviewProfile)
    }

    private fun createViewModel(
        currentMemberId: Long? = null,
        memberProfile: MemberProfile =
            MemberProfile(
                id = 1,
                nickname = "임두현",
                profileImage = null,
                introduction = null,
                instagram = null,
            ),
    ): MyPageViewModel {
        val authRepository =
            object : AuthRepository {
                override suspend fun loginWithKakao(context: Context): Result<KaKaoLoginResponse> =
                    Result.failure(UnsupportedOperationException())

                override suspend fun getKakaoUserInfo(): Result<KakaoUserInfo> =
                    Result.failure(UnsupportedOperationException())

                override suspend fun unlinkKakao(): Result<Unit> = Result.failure(UnsupportedOperationException())

                override fun isKakaoTalkLoginAvailable(context: Context): Boolean = false

                override fun getCurrentMemberId(): Long? = currentMemberId
            }
        val memberRepository =
            object : MemberRepository {
                override suspend fun checkNicknameAvailable(nickname: String): Result<Boolean> = Result.success(true)

                override suspend fun getMemberProfile(memberId: Long): Result<MemberProfile> =
                    Result.success(memberProfile)

                override suspend fun updateMemberProfile(command: UpdateMemberProfileCommand): Result<Unit> =
                    Result.success(Unit)
            }

        return MyPageViewModel(
            getCurrentMemberIdUseCase = GetCurrentMemberIdUseCase(authRepository),
            getMemberProfileUseCase = GetMemberProfileUseCase(memberRepository),
        )
    }

    private fun MyPageViewModel.applyDevPhotographerPreview() {
        handleIntent(
            MyPageIntent.ApplyDevPhotographerPreview(
                hasShootings = false,
                hasPackagePreview = false,
                hasPortfolioPreview = false,
            ),
        )
    }
}
