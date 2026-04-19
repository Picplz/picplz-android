package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.domain.repository.MemberRepository
import com.hm.picplz.domain.usecase.CheckNicknameAvailabilityUseCase
import com.hm.picplz.feature.mypage.R
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
            val viewModel = MyPagePhotographerModifyProfileViewModel(createUseCase())

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeInstagramId("new_instagram"))
            advanceUntilIdle()

            assertTrue(viewModel.state.value.isCompleteEnabled)
        }

    @Test
    fun `nickname whitespace blocks duplicate check and disables complete`() =
        runTest {
            val memberRepository = FakeMemberRepository()
            val viewModel = MyPagePhotographerModifyProfileViewModel(createUseCase(memberRepository))

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
            val viewModel = MyPagePhotographerModifyProfileViewModel(createUseCase(memberRepository))

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeNickname("validnickname"))
            advanceUntilIdle()

            assertEquals(listOf("validnickname"), memberRepository.requestedNicknames)
            assertEquals(
                NicknameFieldError.DuplicateNickname().message,
                viewModel.state.value.representativeNicknameError?.message,
            )
        }

    @Test
    fun `save keeps screen open and exposes unsupported-save message`() =
        runTest {
            val viewModel = MyPagePhotographerModifyProfileViewModel(createUseCase())

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeIntroduction("새 소개글"))
            advanceUntilIdle()
            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.Save)

            assertEquals(
                R.string.modify_profile_save_unsupported,
                viewModel.state.value.saveErrorMessageResId,
            )
            assertTrue(viewModel.state.value.hasChanges)
        }

    private fun createUseCase(memberRepository: FakeMemberRepository = FakeMemberRepository()) =
        CheckNicknameAvailabilityUseCase(memberRepository)

    private class FakeMemberRepository(
        private val isAvailable: Boolean = true,
    ) : MemberRepository {
        val requestedNicknames = mutableListOf<String>()

        override suspend fun checkNicknameAvailable(nickname: String): Result<Boolean> {
            requestedNicknames += nickname
            return Result.success(isAvailable)
        }
    }
}
