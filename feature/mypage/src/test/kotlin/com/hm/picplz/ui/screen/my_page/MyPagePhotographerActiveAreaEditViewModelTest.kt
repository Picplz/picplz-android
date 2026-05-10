package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerDetail
import com.hm.picplz.domain.repository.AreaRepository
import com.hm.picplz.domain.repository.LocationRepository
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetNearbyAreasUseCase
import com.hm.picplz.domain.usecase.GetPhotographerActiveAreasUseCase
import com.hm.picplz.domain.usecase.SearchAreasUseCase
import com.hm.picplz.domain.usecase.UpdatePhotographerActiveAreasUseCase
import com.hm.picplz.feature.mypage.R
import com.kakao.vectormap.LatLng
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
class MyPagePhotographerActiveAreaEditViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial load displays current active areas`() =
        runTest {
            val photographerRepository = FakePhotographerRepository(activeAreas = listOf(area(2), area(1)))
            val viewModel =
                createViewModel(
                    photographerRepository = photographerRepository,
                )
            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.LoadActiveAreas(1))
            advanceUntilIdle()

            assertEquals(1L, photographerRepository.requestedPhotographerId)
            assertEquals(listOf(area(2), area(1)), viewModel.state.value.selectedAreas)
            assertFalse(viewModel.state.value.isSaveEnabled)
        }

    @Test
    fun `selection is capped at five areas`() =
        runTest {
            val viewModel =
                createViewModel(
                    photographerRepository = FakePhotographerRepository(activeAreas = (1L..5L).map(::area)),
                )
            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.LoadActiveAreas(1))
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.ToggleAreaSelection(area(6)))
            advanceUntilIdle()

            assertEquals((1L..5L).map(::area), viewModel.state.value.selectedAreas)
            assertEquals(R.string.active_area_edit_max_count_error, viewModel.state.value.toastMessageResId)
        }

    @Test
    fun `save sends areas in final tag order and navigates back`() =
        runTest {
            val photographerRepository = FakePhotographerRepository(activeAreas = listOf(area(1)))
            val viewModel = createViewModel(photographerRepository = photographerRepository)
            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.LoadActiveAreas(1))
            advanceUntilIdle()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.ToggleAreaSelection(area(2)))
            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.ToggleAreaSelection(area(3)))
            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.Save)
            advanceUntilIdle()

            assertEquals(listOf(area(1), area(2), area(3)), photographerRepository.updatedAreas)
            assertEquals(MyPagePhotographerActiveAreaEditSideEffect.NavigateBack, sideEffectDeferred.await())
        }

    @Test
    fun `search updates results`() =
        runTest {
            val viewModel = createViewModel(areaRepository = FakeAreaRepository(results = listOf(area(10))))
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.UpdateSearchQuery("마포"))
            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.SearchArea)
            advanceUntilIdle()

            assertEquals(listOf(area(10)), viewModel.state.value.searchResults)
            assertTrue(viewModel.state.value.hasSearchCompleted)
        }

    @Test
    fun `blank search loads nearby areas`() =
        runTest {
            val areaRepository = FakeAreaRepository(results = listOf(area(20)))
            val viewModel = createViewModel(areaRepository = areaRepository)
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerActiveAreaEditIntent.SearchArea)
            advanceUntilIdle()

            assertTrue(areaRepository.didLoadNearbyAreas)
            assertEquals(listOf(area(20)), viewModel.state.value.searchResults)
            assertFalse(viewModel.state.value.hasSearchCompleted)
        }

    private fun createViewModel(
        photographerRepository: FakePhotographerRepository = FakePhotographerRepository(),
        areaRepository: FakeAreaRepository = FakeAreaRepository(),
    ): MyPagePhotographerActiveAreaEditViewModel {
        return MyPagePhotographerActiveAreaEditViewModel(
            getPhotographerActiveAreasUseCase = GetPhotographerActiveAreasUseCase(photographerRepository),
            updatePhotographerActiveAreasUseCase = UpdatePhotographerActiveAreasUseCase(photographerRepository),
            searchAreasUseCase = SearchAreasUseCase(areaRepository),
            getNearbyAreasUseCase = GetNearbyAreasUseCase(areaRepository),
            getCurrentLocationUseCase = GetCurrentLocationUseCase(FakeLocationRepository()),
        )
    }

    private fun area(id: Long) =
        Area(
            id = id,
            name = "서울 마포구 $id",
            dong = "마포구 $id",
            ri = null,
        )
}

private class FakePhotographerRepository(
    private val activeAreas: List<Area> = emptyList(),
) : PhotographerRepository {
    var updatedAreas: List<Area> = emptyList()
    var requestedPhotographerId: Long? = null

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers> = error("Not used")

    override suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String,
    ): Result<PhotographerDetail> = error("Not used")

    override suspend fun getActiveAreas(photographerId: Long): Result<List<Area>> {
        requestedPhotographerId = photographerId
        return Result.success(activeAreas)
    }

    override suspend fun updateActiveAreas(areas: List<Area>): Result<List<Area>> {
        updatedAreas = areas
        return Result.success(areas)
    }
}

private class FakeAreaRepository(
    private val results: List<Area> = emptyList(),
) : AreaRepository {
    var didLoadNearbyAreas: Boolean = false

    override suspend fun searchAreas(keyword: String): Result<List<Area>> = Result.success(results)

    override suspend fun getNearbyAreas(
        rad: Int,
        lat: Double,
        lng: Double,
    ): Result<List<Area>> {
        didLoadNearbyAreas = true
        return Result.success(results)
    }
}

private class FakeLocationRepository : LocationRepository {
    override fun getCurrentLocation(
        onLocationReceived: (LatLng) -> Unit,
        onPermissionDenied: () -> Unit,
    ) {
        onLocationReceived(LatLng.from(37.0, 127.0))
    }
}
