package com.hm.picplz.ui.screen.detail_photographer

import androidx.lifecycle.SavedStateHandle
import com.hm.picplz.common.model.PhotoReview
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerDetail
import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.domain.model.PhotographerReview
import com.hm.picplz.domain.model.PhotographerReviewData
import com.hm.picplz.domain.model.PhotographerReviewSummary
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.usecase.GetPhotographerDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailPhotographerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `preview mode does not load photographer info`() =
        runTest {
            val repository = FakePhotographerRepository()

            DetailPhotographerViewModel(
                savedStateHandle = previewSavedStateHandle(),
                getPhotographerDetailUseCase = GetPhotographerDetailUseCase(repository),
            )
            advanceUntilIdle()

            assertEquals(0, repository.getPhotographerDetailCallCount)
        }

    @Test
    fun `preview mode starts with empty reviews and portfolios`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = previewSavedStateHandle(),
                    getPhotographerDetailUseCase = GetPhotographerDetailUseCase(FakePhotographerRepository()),
                )

            assertTrue(viewModel.state.value.isPreviewMode)
            assertTrue(viewModel.state.value.reviews.isEmpty())
            assertTrue(viewModel.state.value.portfolios.isEmpty())
            assertEquals(0, viewModel.state.value.reviewSummary.totalReviewCount)
            assertEquals(0, viewModel.state.value.reviewSummary.totalPhotoReviewCount)
            assertTrue(viewModel.state.value.reviewSummary.photoReviews.isEmpty())
            assertTrue(viewModel.state.value.profileInfo.photoPortfolios.isEmpty())
            assertTrue(viewModel.state.value.shootingPackages.isEmpty())
        }

    @Test
    fun `normal mode loads photographer detail into state`() =
        runTest {
            val repository = FakePhotographerRepository(Result.success(detailFixture()))

            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = normalSavedStateHandle(),
                    getPhotographerDetailUseCase = GetPhotographerDetailUseCase(repository),
                )
            advanceUntilIdle()

            val state = viewModel.state.value
            assertEquals(1, repository.getPhotographerDetailCallCount)
            assertFalse(state.isLoading)
            assertEquals("유가영", state.profileInfo.name)
            assertTrue(state.isFollow)
            assertEquals(1, state.reviews.size)
            assertEquals(4.5f, state.reviewSummary.averageRating)
            assertEquals(1, state.shootingPackages.size)
        }

    @Test
    fun `booking in preview mode shows preview action dialog`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = previewSavedStateHandle(),
                    getPhotographerDetailUseCase = GetPhotographerDetailUseCase(FakePhotographerRepository()),
                )

            viewModel.handleIntent(DetailPhotographerIntent.SelectBooking)
            advanceUntilIdle()

            assertEquals(DetailPreviewAction.Booking, viewModel.state.value.previewActionDialog)
        }

    @Test
    fun `follow in preview mode shows dialog without changing follow state`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = previewSavedStateHandle(),
                    getPhotographerDetailUseCase = GetPhotographerDetailUseCase(FakePhotographerRepository()),
                )
            val initialFollowState = viewModel.state.value.isFollow

            viewModel.handleIntent(DetailPhotographerIntent.ToggleFollow)
            advanceUntilIdle()

            assertEquals(DetailPreviewAction.Follow, viewModel.state.value.previewActionDialog)
            assertEquals(initialFollowState, viewModel.state.value.isFollow)
        }

    @Test
    fun `normal mode follow toggles follow state`() =
        runTest {
            val repository = FakePhotographerRepository()
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = normalSavedStateHandle(),
                    getPhotographerDetailUseCase = GetPhotographerDetailUseCase(repository),
                )
            advanceUntilIdle()
            val initialFollowState = viewModel.state.value.isFollow

            viewModel.handleIntent(DetailPhotographerIntent.ToggleFollow)

            assertFalse(viewModel.state.value.isPreviewMode)
            assertEquals(1, repository.getPhotographerDetailCallCount)
            assertTrue(viewModel.state.value.previewActionDialog == null)
            assertEquals(!initialFollowState, viewModel.state.value.isFollow)
        }

    @Test
    fun `booking in normal mode keeps existing no-op behavior`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = normalSavedStateHandle(),
                    getPhotographerDetailUseCase = GetPhotographerDetailUseCase(FakePhotographerRepository()),
                )

            viewModel.handleIntent(DetailPhotographerIntent.SelectBooking)
            advanceUntilIdle()

            assertTrue(viewModel.state.value.previewActionDialog == null)
        }

    private fun previewSavedStateHandle() =
        SavedStateHandle(
            mapOf(
                "photographerId" to 1,
                "previewMode" to true,
            ),
        )

    private fun normalSavedStateHandle() =
        SavedStateHandle(
            mapOf(
                "photographerId" to 1,
                "previewMode" to false,
            ),
        )
}

private class FakePhotographerRepository(
    private val detailResult: Result<PhotographerDetail> = Result.failure(NotImplementedError()),
) : PhotographerRepository {
    var getPhotographerDetailCallCount = 0
        private set

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers> = Result.failure(NotImplementedError())

    override suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String,
    ): Result<PhotographerDetail> {
        getPhotographerDetailCallCount += 1
        return detailResult
    }
}

private fun detailFixture() =
    PhotographerDetail(
        profileInfo =
            PhotographerInfo(
                id = 7,
                name = "유가영",
                socialAccount = "imdooring",
                infoText = "소개",
                isActive = true,
                isBookable = true,
                isFollow = true,
                followCount = 3,
                profileImageUri = "https://example.com/profile.jpg",
                workingArea = listOf("마포구"),
                keyword = listOf("캐주얼"),
                equipment = emptyList(),
                photoPortfolios = emptyList(),
            ),
        reviewData =
            PhotographerReviewData(
                summary =
                    PhotographerReviewSummary(
                        averageRating = 4.5f,
                        keywordBars = emptyList(),
                        totalReviewCount = 1,
                        totalPhotoReviewCount = 1,
                        photoReviews =
                            listOf(
                                PhotoReview(
                                    reviewId = 1,
                                    photoReviewUri = "https://example.com/review.jpg",
                                    index = 0,
                                ),
                            ),
                    ),
                reviews =
                    listOf(
                        PhotographerReview(
                            reviewId = 1,
                            profileImageUri = "https://example.com/customer.jpg",
                            nickname = "사용자1",
                            rating = 4.5f,
                            createdAt = "2026-05-10T00:00:00",
                            isReported = false,
                            photoReviews = emptyList(),
                            photoReviewCount = 0,
                            option = "",
                            location = "",
                            reviewText = "좋았어요",
                            isRecommended = false,
                            recommendationCount = 0,
                        ),
                    ),
            ),
        shootingPackages =
            listOf(
                ShootingPackage(
                    packageId = 1,
                    title = "남친 생기는 프사",
                    price = 9900,
                    imageUri = "https://example.com/package.jpg",
                    shootingTime = "15분",
                    description = "상품 설명",
                ),
            ),
    )
