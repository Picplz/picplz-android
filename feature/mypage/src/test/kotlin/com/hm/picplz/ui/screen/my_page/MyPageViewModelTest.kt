package com.hm.picplz.ui.screen.my_page

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `navigate to followed photographers emits navigation side effect`() =
        runTest {
            val viewModel = MyPageViewModel()
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
            val viewModel = MyPageViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerModifyProfile)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToPhotographerModifyProfile,
                sideEffectDeferred.await(),
            )
        }
}
