package com.hm.picplz.ui.screen.my_page

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

    @Test
    fun `navigate to package edit emits dedicated navigation side effect`() =
        runTest {
            val viewModel = MyPageViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPackageEdit)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToPackageEdit(1),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `navigate to photographer keyword edit emits dedicated navigation side effect`() =
        runTest {
            val viewModel = MyPageViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerKeywordEdit)
            advanceUntilIdle()

            val sideEffect = sideEffectDeferred.await()
            assertTrue(sideEffect is MyPageSideEffect.NavigateToPhotographerKeywordEdit)
            assertEquals(1, (sideEffect as MyPageSideEffect.NavigateToPhotographerKeywordEdit).photographerId)
        }

    @Test
    fun `navigate to photographer region edit emits dedicated navigation side effect`() =
        runTest {
            val viewModel = MyPageViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerRegionEdit)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.NavigateToPhotographerActiveAreaEdit(1),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `photographer preview without package emits requires package toast`() =
        runTest {
            val viewModel = MyPageViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerPreview)
            advanceUntilIdle()

            assertEquals(
                MyPageSideEffect.ShowToast(com.hm.picplz.feature.mypage.R.string.my_page_preview_requires_package),
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `photographer preview with package emits detail navigation side effect`() =
        runTest {
            val viewModel = MyPageViewModel()
            viewModel.handleIntent(
                MyPageIntent.ApplyDevPhotographerPreview(
                    hasShootings = false,
                    hasPackagePreview = true,
                    hasPortfolioPreview = false,
                ),
            )
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPageIntent.NavigateToPhotographerPreview)
            advanceUntilIdle()

            val sideEffect = sideEffectDeferred.await()
            assertTrue(sideEffect is MyPageSideEffect.NavigateToPhotographerPreview)
            assertEquals(1, (sideEffect as MyPageSideEffect.NavigateToPhotographerPreview).photographerId)
        }

    @Test
    fun `apply photographer keyword summary updates profile summary`() {
        val viewModel = MyPageViewModel()

        viewModel.handleIntent(MyPageIntent.ApplyPhotographerKeywordSummary("#캐주얼, #심플"))

        assertEquals("#캐주얼, #심플", viewModel.state.value.photographerProfile.keywordSummary)
    }

    @Test
    fun `photographer preview availability requires package and photographer id`() {
        assertFalse(PhotographerProfile(hasPackages = false, photographerId = 1).canPreviewProfile)
        assertFalse(PhotographerProfile(hasPackages = true, photographerId = 0).canPreviewProfile)
        assertTrue(PhotographerProfile(hasPackages = true, photographerId = 1).canPreviewProfile)
    }
}
