package com.hm.picplz.ui.screen.my_page

import android.content.Context
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.model.UpdateMemberProfileCommand
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.repository.MemberRepository
import com.hm.picplz.domain.repository.S3Repository
import com.hm.picplz.domain.usecase.CheckNicknameAvailabilityUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetMemberProfileUseCase
import com.hm.picplz.domain.usecase.UpdateMemberProfileUseCase
import com.hm.picplz.domain.usecase.UploadProfileImageUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPagePhotographerModifyProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `instagram change enables complete when nickname remains valid`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeInstagramId("new_instagram"))
            advanceUntilIdle()

            assertTrue(viewModel.state.value.isCompleteEnabled)
        }

    @Test
    fun `nickname whitespace blocks duplicate check and disables complete`() =
        runTest {
            val memberRepository = FakeMemberRepository()
            val viewModel = createViewModel(memberRepository = memberRepository)
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeNickname("ab cd"))
            advanceUntilIdle()

            assertEquals(0, memberRepository.requestedNicknames.size)
            assertEquals(
                NicknameFieldError.Whitespace().message,
                viewModel.state.value.representativeNicknameError?.message,
            )
            assertFalse(viewModel.state.value.isCompleteEnabled)
        }

    @Test
    fun `duplicate nickname becomes representative error after local validation passes`() =
        runTest {
            val memberRepository = FakeMemberRepository(isAvailable = false)
            val viewModel = createViewModel(memberRepository = memberRepository)
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeNickname("validnickname"))
            advanceUntilIdle()

            assertEquals(listOf("validnickname"), memberRepository.requestedNicknames)
            assertEquals(
                NicknameFieldError.DuplicateNickname().message,
                viewModel.state.value.representativeNicknameError?.message,
            )
        }

    @Test
    fun `save updates original values when request succeeds`() =
        runTest {
            val memberRepository = FakeMemberRepository()
            val viewModel = createViewModel(memberRepository = memberRepository)
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeIntroduction("새 소개글"))
            advanceUntilIdle()
            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.Save)
            advanceUntilIdle()

            assertEquals(
                "새 소개글",
                memberRepository.updatedCommand?.introduction,
            )
            assertFalse(viewModel.state.value.hasChanges)
        }

    private fun createViewModel(
        memberRepository: FakeMemberRepository = FakeMemberRepository(),
        memberId: Long = 1L,
    ): MyPagePhotographerModifyProfileViewModel =
        MyPagePhotographerModifyProfileViewModel(
            checkNicknameAvailabilityUseCase = CheckNicknameAvailabilityUseCase(memberRepository),
            getCurrentMemberIdUseCase = GetCurrentMemberIdUseCase(FakeAuthRepository(memberId)),
            getMemberProfileUseCase = GetMemberProfileUseCase(memberRepository),
            updateMemberProfileUseCase = UpdateMemberProfileUseCase(memberRepository),
            uploadProfileImageUseCase = UploadProfileImageUseCase(FakeS3Repository()),
        )

    private class FakeMemberRepository(
        private val isAvailable: Boolean = true,
    ) : MemberRepository {
        val requestedNicknames = mutableListOf<String>()
        var updatedCommand: UpdateMemberProfileCommand? = null

        override suspend fun checkNicknameAvailable(nickname: String): Result<Boolean> {
            requestedNicknames += nickname
            return Result.success(isAvailable)
        }

        override suspend fun getMemberProfile(memberId: Long): Result<MemberProfile> =
            Result.success(
                MemberProfile(
                    id = memberId,
                    nickname = "유가영",
                    profileImage = null,
                    introduction = "안녕하세요, 임두현 사진작가입니다.",
                    instagram = "imdooring",
                ),
            )

        override suspend fun updateMemberProfile(command: UpdateMemberProfileCommand): Result<Unit> {
            updatedCommand = command
            return Result.success(Unit)
        }
    }

    private class FakeAuthRepository(
        private val memberId: Long,
    ) : AuthRepository {
        override suspend fun loginWithKakao(context: Context) = error("unused")

        override suspend fun getKakaoUserInfo() = error("unused")

        override suspend fun unlinkKakao() = error("unused")

        override fun isKakaoTalkLoginAvailable(context: Context) = false

        override fun getCurrentMemberId(): Long = memberId
    }

    private class FakeS3Repository : S3Repository {
        override suspend fun uploadProfileImage(
            imageBytes: ByteArray,
            filename: String,
        ): Result<String> = Result.success("profile/object-key.jpg")
    }
}
