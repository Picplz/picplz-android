package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import android.util.Log
import com.hm.picplz.common.model.User
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.CameraListData
import com.hm.picplz.data.model.DeviceBrand
import com.hm.picplz.data.provider.SocialInfo
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.AddressService
import com.hm.picplz.data.service.CameraService
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.domain.model.PhotographerReviewData
import com.hm.picplz.domain.model.PhotographerSignup
import com.hm.picplz.domain.model.PhotographerSignupCameraType
import com.hm.picplz.ui.screen.login.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpPhotographerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockedLog: MockedStatic<Log>

    @After
    fun tearDown() {
        if (::mockedLog.isInitialized) {
            mockedLog.close()
        }
    }

    @Test
    fun `submit creates photographer signup and navigates to completion`() =
        runTest {
            mockAndroidLog()
            val photographerService = FakePhotographerService(createPhotographerResult = Result.success(Unit))
            val viewModel =
                SignUpPhotographerViewModel(
                    addressService = FakeAddressService(),
                    locationService = mock(LocationService::class.java),
                    cameraService = FakeCameraService(),
                    photographerService = photographerService,
                    tokenManager =
                        buildTokenManager(
                            socialCode = "SOCIAL_CODE",
                            socialEmail = "photographer@example.com",
                            socialProvider = "KAKAO",
                        ),
                )
            val user =
                User(
                    id = "user-id",
                    nickname = "pic작가",
                    profileImageUri = "https://example.com/profile.jpg",
                    profileImageObjectKey = "profile/object-key.jpg",
                )
            val sideEffect = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(SignUpPhotographerIntent.SetUserInfo(user))
            viewModel.handleIntent(
                SignUpPhotographerIntent.ToggleAreaSelection(
                    Area(
                        id = 110L,
                        name = "서울 마포구",
                        dong = "마포구",
                        ri = null,
                    ),
                ),
            )
            viewModel.handleIntent(
                SignUpPhotographerIntent.AddDeviceToCategory(
                    Device.PhoneDevice(
                        id = "phone-id",
                        companyName = "애플",
                        modelName = "아이폰 15",
                    ),
                ),
            )
            viewModel.handleIntent(SignUpPhotographerIntent.NavigateWithSubmit)
            advanceUntilIdle()

            val signup = photographerService.lastSignup ?: error("photographer signup should be submitted")

            assertEquals(SignUpPhotographerSideEffect.NavigateToSignUpCompletion(user), sideEffect.await())
            assertEquals(1, photographerService.createPhotographerCallCount)
            assertEquals("pic작가", signup.nickname)
            assertEquals("photographer@example.com", signup.socialEmail)
            assertEquals("KAKAO", signup.socialProvider)
            assertEquals("SOCIAL_CODE", signup.socialCode)
            assertEquals("profile/object-key.jpg", signup.profileImage)
            assertEquals(110L, signup.activeAreas.single().code)
            assertEquals(1, signup.activeAreas.single().priority)
            assertEquals(PhotographerSignupCameraType.PHONE, signup.cameras.single().type)
            assertEquals("애플", signup.cameras.single().brand)
            assertEquals("아이폰 15", signup.cameras.single().name)
            assertEquals(false, viewModel.state.value.isSubmitting)
        }

    private fun mockAndroidLog() {
        mockedLog = Mockito.mockStatic(Log::class.java)
        mockedLog.`when`<Int> { Log.d(anyString(), anyString()) }.thenReturn(0)
        mockedLog.`when`<Int> { Log.w(anyString(), anyString()) }.thenReturn(0)
        mockedLog
            .`when`<Int> { Log.e(anyString(), anyString(), any(Throwable::class.java)) }
            .thenReturn(0)
    }

    private fun buildTokenManager(
        socialCode: String?,
        socialEmail: String?,
        socialProvider: String?,
    ): TokenManager {
        val tokenManager = mock(TokenManager::class.java)
        `when`(tokenManager.getSocialInfo()).thenReturn(
            SocialInfo(
                code = socialCode,
                email = socialEmail,
                provider = socialProvider,
            ),
        )
        return tokenManager
    }

    private class FakeCameraService : CameraService {
        override suspend fun getCameraList(): AppResult<CameraListData> =
            Result.success(
                CameraListData(
                    brands = listOf(DeviceBrand("애플", listOf("아이폰 15"))),
                    types = listOf("미러리스 카메라"),
                ),
            )
    }

    private class FakeAddressService : AddressService {
        override suspend fun searchArea(keyword: String): AppResult<List<Area>> = Result.success(emptyList())

        override suspend fun getNearbyAreas(
            rad: Int,
            lat: Double,
            lng: Double,
        ): AppResult<List<Area>> = Result.success(emptyList())
    }

    private class FakePhotographerService(
        private val createPhotographerResult: AppResult<Unit>,
    ) : PhotographerService {
        var createPhotographerCallCount = 0
            private set
        var lastSignup: PhotographerSignup? = null
            private set

        override suspend fun createPhotographer(signup: PhotographerSignup): AppResult<Unit> {
            createPhotographerCallCount++
            lastSignup = signup
            return createPhotographerResult
        }

        override suspend fun getPhotographerMoodKeywords(photographerId: Long): AppResult<List<String>> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun addPhotoMood(photoMood: String): AppResult<Unit> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun deletePhotoMood(photoMood: String): AppResult<Unit> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun updateActiveAreas(areas: List<Area>): AppResult<List<Area>> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): AppResult<FilteredPhotographers> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getPhotographerInfo(photographerId: Long): AppResult<PhotographerInfo> {
            throw NotImplementedError("Not used in test")
        }

        override suspend fun getPhotographerReviews(
            photographerId: Long,
            page: Int,
            size: Int,
            sort: String,
        ): AppResult<PhotographerReviewData> {
            throw NotImplementedError("Not used in test")
        }
    }
}
