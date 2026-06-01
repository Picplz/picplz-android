package com.hm.picplz.ui.screen.my_page

import android.content.Context
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.result.AppResult
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
import com.hm.picplz.feature.mypage.R
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
import java.util.ArrayDeque

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

    @Test
    fun `load failure shows toast and does not keep inline save error`() =
        runTest {
            val memberRepository = FakeMemberRepository(loadShouldFail = true)
            val viewModel = createViewModel(memberRepository = memberRepository)
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            advanceUntilIdle()

            assertEquals(null, viewModel.state.value.saveErrorMessageResId)
            assertEquals(
                MyPagePhotographerModifyProfileSideEffect.ShowToast(R.string.modify_profile_load_failed),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `click profile image launches image picker side effect`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(
                MyPagePhotographerModifyProfileIntent.SyncPhotoPermissionState(
                    granted = true,
                    hasRequested = true,
                    permanentlyDenied = false,
                ),
            )

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ClickProfileImage)
            advanceUntilIdle()

            assertEquals(
                MyPagePhotographerModifyProfileSideEffect.LaunchImagePicker,
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `photo permission action requests permission when settings redirect is not needed`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(
                MyPagePhotographerModifyProfileIntent.SyncPhotoPermissionState(
                    granted = false,
                    hasRequested = false,
                    permanentlyDenied = false,
                ),
            )
            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ClickPhotoPermissionAction)
            advanceUntilIdle()

            assertEquals(
                MyPagePhotographerModifyProfileSideEffect.RequestPhotoPermission,
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `photo permission action opens settings when permission is permanently denied`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(
                MyPagePhotographerModifyProfileIntent.SyncPhotoPermissionState(
                    granted = false,
                    hasRequested = true,
                    permanentlyDenied = true,
                ),
            )
            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ClickPhotoPermissionAction)
            advanceUntilIdle()

            assertEquals(
                MyPagePhotographerModifyProfileSideEffect.OpenPhotoPermissionSettings,
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `upload failure restores previous preview and clears image-only dirty state`() =
        runTest {
            val viewModel =
                createViewModel(
                    s3Repository =
                        FakeS3Repository(
                            uploadResults = listOf(Result.failure(IllegalStateException("upload failed"))),
                        ),
                )
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeProfileImage("content://picked-image"))
            viewModel.handleIntent(
                MyPagePhotographerModifyProfileIntent.UploadProfileImage(
                    imageBytes = byteArrayOf(1),
                    filename = "profile.jpg",
                ),
            )
            advanceUntilIdle()

            assertEquals("", viewModel.state.value.profileImageUri)
            assertEquals(null, viewModel.state.value.profileImageObjectKey)
            assertEquals(R.string.modify_profile_image_upload_failed, viewModel.state.value.saveErrorMessageResId)
            assertFalse(viewModel.state.value.isCompleteEnabled)
        }

    @Test
    fun `upload failure after previous success restores last valid preview and preserves object key`() =
        runTest {
            val viewModel =
                createViewModel(
                    s3Repository =
                        FakeS3Repository(
                            uploadResults =
                                listOf(
                                    Result.success("profile/first-object-key.jpg"),
                                    Result.failure(IllegalStateException("upload failed")),
                                ),
                        ),
                )
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeProfileImage("content://first-image"))
            viewModel.handleIntent(
                MyPagePhotographerModifyProfileIntent.UploadProfileImage(
                    imageBytes = byteArrayOf(1),
                    filename = "first.jpg",
                ),
            )
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerModifyProfileIntent.ChangeProfileImage("content://second-image"))
            viewModel.handleIntent(
                MyPagePhotographerModifyProfileIntent.UploadProfileImage(
                    imageBytes = byteArrayOf(2),
                    filename = "second.jpg",
                ),
            )
            advanceUntilIdle()

            assertEquals("content://first-image", viewModel.state.value.profileImageUri)
            assertEquals("profile/first-object-key.jpg", viewModel.state.value.profileImageObjectKey)
            assertEquals(R.string.modify_profile_image_upload_failed, viewModel.state.value.saveErrorMessageResId)
            assertTrue(viewModel.state.value.isCompleteEnabled)
        }

    private fun createViewModel(
        memberRepository: FakeMemberRepository = FakeMemberRepository(),
        memberId: Long = 1L,
        s3Repository: FakeS3Repository = FakeS3Repository(),
    ): MyPagePhotographerModifyProfileViewModel =
        MyPagePhotographerModifyProfileViewModel(
            checkNicknameAvailabilityUseCase = CheckNicknameAvailabilityUseCase(memberRepository),
            getCurrentMemberIdUseCase = GetCurrentMemberIdUseCase(FakeAuthRepository(memberId)),
            getMemberProfileUseCase = GetMemberProfileUseCase(memberRepository),
            updateMemberProfileUseCase = UpdateMemberProfileUseCase(memberRepository),
            uploadProfileImageUseCase = UploadProfileImageUseCase(s3Repository),
        )

    private class FakeMemberRepository(
        private val isAvailable: Boolean = true,
        private val loadShouldFail: Boolean = false,
    ) : MemberRepository {
        val requestedNicknames = mutableListOf<String>()
        var updatedCommand: UpdateMemberProfileCommand? = null

        override suspend fun checkNicknameAvailable(nickname: String): AppResult<Boolean> {
            requestedNicknames += nickname
            return Result.success(isAvailable)
        }

        override suspend fun getMemberProfile(memberId: Long): AppResult<MemberProfile> =
            if (loadShouldFail) {
                Result.failure(IllegalStateException("load failed"))
            } else {
                Result.success(
                    MemberProfile(
                        id = memberId,
                        nickname = "유가영",
                        profileImage = null,
                        introduction = "안녕하세요, 임두현 사진작가입니다.",
                        instagram = "imdooring",
                    ),
                )
            }

        override suspend fun updateMemberProfile(command: UpdateMemberProfileCommand): AppResult<Unit> {
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
        private val uploadResults = ArrayDeque<AppResult<String>>()

        constructor(
            uploadResults: List<AppResult<String>> = listOf(Result.success("profile/object-key.jpg")),
        ) {
            this.uploadResults.addAll(uploadResults)
        }

        override suspend fun uploadProfileImage(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> =
            if (uploadResults.isEmpty()) {
                Result.success("profile/object-key.jpg")
            } else {
                uploadResults.removeFirst()
            }

        override suspend fun uploadProductImage(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> = error("unused")
    }
}
