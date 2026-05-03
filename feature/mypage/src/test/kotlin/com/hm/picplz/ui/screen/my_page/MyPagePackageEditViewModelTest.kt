package com.hm.picplz.ui.screen.my_page

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPagePackageEditViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state disables save`() {
        val viewModel = MyPagePackageEditViewModel()

        assertFalse(viewModel.state.value.isSaveEnabled)
    }

    @Test
    fun `navigate back emits navigation side effect`() =
        runTest {
            val viewModel = MyPagePackageEditViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPagePackageEditIntent.NavigateBack)
            advanceUntilIdle()

            assertEquals(
                MyPagePackageEditSideEffect.NavigateBack,
                sideEffectDeferred.await(),
            )
        }
}
