package com.hm.picplz.ui.screen.my_page

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FollowedPhotographersViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial load finishes and exposes followed photographers state`() =
        runTest {
            val viewModel = FollowedPhotographersViewModel()

            assertTrue(viewModel.state.value.isLoading)

            advanceTimeBy(350)
            advanceUntilIdle()

            assertFalse(viewModel.state.value.isLoading)
            assertTrue(viewModel.state.value.photographers.isNotEmpty())
            assertFalse(viewModel.state.value.isUnavailable)
        }

    @Test
    fun `release loaded state is unavailable instead of empty follows state`() {
        val state = FollowedPhotographersViewModel.loadedState(isDebugBuild = false)

        assertFalse(state.isLoading)
        assertTrue(state.isUnavailable)
        assertTrue(state.photographers.isEmpty())
    }

    @Test
    fun `select photographer emits detail navigation side effect`() =
        runTest {
            val viewModel = FollowedPhotographersViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(FollowedPhotographersIntent.SelectPhotographer(5))
            advanceUntilIdle()

            assertTrue(
                sideEffectDeferred.await() is
                    FollowedPhotographersSideEffect.NavigateToPhotographerDetail,
            )
        }

    @Test
    fun `navigate back emits back side effect`() =
        runTest {
            val viewModel = FollowedPhotographersViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(FollowedPhotographersIntent.NavigateBack)
            advanceUntilIdle()

            assertEquals(FollowedPhotographersSideEffect.NavigateBack, sideEffectDeferred.await())
        }
}
