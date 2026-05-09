package com.hm.picplz.ui.screen.detail_photographer

import androidx.lifecycle.SavedStateHandle
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.data.model.PhotographerRatingDto
import com.hm.picplz.data.model.PortfolioDto
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.FilteredPhotographers
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
            val service = FakePhotographerService()

            DetailPhotographerViewModel(
                savedStateHandle = previewSavedStateHandle(),
                photographerService = service,
            )
            advanceUntilIdle()

            assertEquals(0, service.getPhotographerInfoCallCount)
        }

    @Test
    fun `preview mode starts with empty reviews and portfolios`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = previewSavedStateHandle(),
                    photographerService = FakePhotographerService(),
                )

            assertTrue(viewModel.state.value.isPreviewMode)
            assertTrue(viewModel.state.value.reviews.isEmpty())
            assertTrue(viewModel.state.value.portfolios.isEmpty())
            assertEquals(0, viewModel.state.value.reviewSummary.totalReviewCount)
            assertEquals(0, viewModel.state.value.reviewSummary.totalPhotoReviewCount)
            assertTrue(viewModel.state.value.reviewSummary.photoReviews.isEmpty())
            assertTrue(viewModel.state.value.profileInfo.photoPortfolios.isEmpty())
            assertTrue(viewModel.state.value.shootingPackages.isNotEmpty())
        }

    @Test
    fun `booking in preview mode shows preview action dialog`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = previewSavedStateHandle(),
                    photographerService = FakePhotographerService(),
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
                    photographerService = FakePhotographerService(),
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
            val service = FakePhotographerService()
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = normalSavedStateHandle(),
                    photographerService = service,
                )
            advanceUntilIdle()
            val initialFollowState = viewModel.state.value.isFollow

            viewModel.handleIntent(DetailPhotographerIntent.ToggleFollow)

            assertFalse(viewModel.state.value.isPreviewMode)
            assertEquals(1, service.getPhotographerInfoCallCount)
            assertTrue(viewModel.state.value.previewActionDialog == null)
            assertEquals(!initialFollowState, viewModel.state.value.isFollow)
        }

    @Test
    fun `booking in normal mode keeps existing no-op behavior`() =
        runTest {
            val viewModel =
                DetailPhotographerViewModel(
                    savedStateHandle = normalSavedStateHandle(),
                    photographerService = FakePhotographerService(),
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

private class FakePhotographerService : PhotographerService {
    var getPhotographerInfoCallCount = 0
        private set

    override suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit> = Result.success(Unit)

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers> = Result.failure(NotImplementedError())

    override suspend fun getPhotographerInfo(photographerId: Long): Result<PhotographerInfo> {
        getPhotographerInfoCallCount += 1
        return Result.failure(NotImplementedError())
    }

    override suspend fun getPhotographerRating(photographerId: Long): Result<PhotographerRatingDto> =
        Result.failure(NotImplementedError())

    override suspend fun getPhotographerReviews(
        photographerId: Long,
        page: Int,
        size: Int,
        sort: String,
    ): Result<ReviewListDto> = Result.failure(NotImplementedError())

    override suspend fun getPhotographerProducts(photographerId: Long): Result<List<ProductDto>> =
        Result.failure(NotImplementedError())

    override suspend fun getPortfolio(portfolioId: Long): Result<PortfolioDto> = Result.failure(NotImplementedError())
}
