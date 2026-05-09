package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.usecase.GetPhotographerMoodKeywordsUseCase
import com.hm.picplz.domain.usecase.UpdatePhotographerMoodKeywordsUseCase
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
class MyPagePhotographerKeywordEditViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load keywords selects existing default and custom keywords`() =
        runTest {
            val viewModel = createViewModel(keywords = listOf("캐주얼", "필름감성"))

            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()

            val state = viewModel.state.value
            assertFalse(state.isLoading)
            assertTrue(state.selectedKeywordChipList.any { it.label == "캐주얼" })
            assertTrue(state.selectedKeywordChipList.any { it.label == "필름감성" })
            assertTrue(state.keywordChipList.any { it.label == "필름감성" && it.isEditable })
            assertFalse(state.hasChanges)
        }

    @Test
    fun `add duplicate keyword shows duplicate toast`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerKeywordEditIntent.AddKeywordChip("캐주얼"))
            advanceUntilIdle()

            assertTrue(viewModel.state.value.showToast)
            assertEquals(
                com.hm.picplz.feature.mypage.R.string.my_page_keyword_edit_duplicate,
                viewModel.state.value.toastMessageResId,
            )
        }

    @Test
    fun `add custom keyword selects new editable keyword`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()

            viewModel.handleIntent(MyPagePhotographerKeywordEditIntent.AddKeywordChip("필름감성"))

            assertTrue(viewModel.state.value.keywordChipList.any { it.label == "필름감성" && it.isEditable })
            assertTrue(viewModel.state.value.selectedKeywordChipList.any { it.label == "필름감성" })
        }

    @Test
    fun `update custom keyword updates selected keyword label`() =
        runTest {
            val viewModel = createViewModel(keywords = listOf("필름감성"))
            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()
            val customChip = viewModel.state.value.keywordChipList.first { it.label == "필름감성" }

            viewModel.handleIntent(
                MyPagePhotographerKeywordEditIntent.UpdateKeywordChip(
                    chipId = customChip.id,
                    label = "몽환필름",
                ),
            )

            assertTrue(viewModel.state.value.keywordChipList.any { it.label == "몽환필름" })
            assertTrue(viewModel.state.value.selectedKeywordChipList.any { it.label == "몽환필름" })
        }

    @Test
    fun `load failure shows load failure toast`() =
        runTest {
            val viewModel =
                createViewModel(
                    repository = FakePhotographerRepository(failsOnGet = true),
                )

            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()

            assertEquals(
                com.hm.picplz.feature.mypage.R.string.my_page_keyword_edit_load_failed,
                viewModel.state.value.toastMessageResId,
            )
        }

    @Test
    fun `save keywords applies added and removed diff then navigates back`() =
        runTest {
            val repository = FakePhotographerRepository(keywords = listOf("캐주얼"))
            val viewModel = createViewModel(repository = repository)
            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()
            val casualChip = viewModel.state.value.keywordChipList.first { it.label == "캐주얼" }
            val simpleChip = viewModel.state.value.keywordChipList.first { it.label == "심플" }
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(
                MyPagePhotographerKeywordEditIntent.ToggleKeywordSelection(casualChip.id, casualChip.label),
            )
            viewModel.handleIntent(
                MyPagePhotographerKeywordEditIntent.ToggleKeywordSelection(simpleChip.id, simpleChip.label),
            )
            viewModel.handleIntent(MyPagePhotographerKeywordEditIntent.SaveKeywords)
            advanceUntilIdle()

            assertEquals(listOf("심플"), repository.addedKeywords)
            assertEquals(listOf("캐주얼"), repository.deletedKeywords)
            assertEquals(MyPagePhotographerKeywordEditSideEffect.NavigateToPrev, sideEffectDeferred.await())
        }

    @Test
    fun `save failure reloads server keywords and shows failure toast`() =
        runTest {
            val repository = FakePhotographerRepository(keywords = listOf("캐주얼"), failsOnAdd = true)
            val viewModel = createViewModel(repository = repository)
            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()
            val simpleChip = viewModel.state.value.keywordChipList.first { it.label == "심플" }

            viewModel.handleIntent(
                MyPagePhotographerKeywordEditIntent.ToggleKeywordSelection(simpleChip.id, simpleChip.label),
            )
            viewModel.handleIntent(MyPagePhotographerKeywordEditIntent.SaveKeywords)
            advanceUntilIdle()

            assertTrue(viewModel.state.value.selectedKeywordChipList.any { it.label == "캐주얼" })
            assertFalse(viewModel.state.value.selectedKeywordChipList.any { it.label == "심플" })
            assertEquals(
                com.hm.picplz.feature.mypage.R.string.my_page_keyword_edit_save_failed,
                viewModel.state.value.toastMessageResId,
            )
        }

    @Test
    fun `save failure with reload failure resets to original keywords`() =
        runTest {
            val repository =
                FakePhotographerRepository(
                    keywords = listOf("캐주얼"),
                    failsOnAdd = true,
                    failsOnSecondGet = true,
                )
            val viewModel = createViewModel(repository = repository)
            viewModel.handleIntent(loadIntent())
            advanceUntilIdle()
            val simpleChip = viewModel.state.value.keywordChipList.first { it.label == "심플" }

            viewModel.handleIntent(
                MyPagePhotographerKeywordEditIntent.ToggleKeywordSelection(simpleChip.id, simpleChip.label),
            )
            viewModel.handleIntent(MyPagePhotographerKeywordEditIntent.SaveKeywords)
            advanceUntilIdle()

            assertTrue(viewModel.state.value.selectedKeywordChipList.any { it.label == "캐주얼" })
            assertFalse(viewModel.state.value.selectedKeywordChipList.any { it.label == "심플" })
        }

    @Test
    fun `navigate to prev emits navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPagePhotographerKeywordEditIntent.NavigateToPrev)
            advanceUntilIdle()

            assertEquals(MyPagePhotographerKeywordEditSideEffect.NavigateToPrev, sideEffectDeferred.await())
        }

    private fun createViewModel(
        keywords: List<String> = emptyList(),
        repository: FakePhotographerRepository = FakePhotographerRepository(keywords),
    ) = MyPagePhotographerKeywordEditViewModel(
        getPhotographerMoodKeywordsUseCase = GetPhotographerMoodKeywordsUseCase(repository),
        updatePhotographerMoodKeywordsUseCase = UpdatePhotographerMoodKeywordsUseCase(repository),
    )

    private fun loadIntent() =
        MyPagePhotographerKeywordEditIntent.LoadKeywords(
            photographerId = 1L,
            defaultKeywords = DEFAULT_KEYWORDS,
        )

    private companion object {
        val DEFAULT_KEYWORDS = listOf("캐주얼", "고급미", "심플")
    }
}

private class FakePhotographerRepository(
    private val keywords: List<String> = emptyList(),
    private val failsOnAdd: Boolean = false,
    private val failsOnGet: Boolean = false,
    private val failsOnSecondGet: Boolean = false,
) : PhotographerRepository {
    val addedKeywords = mutableListOf<String>()
    val deletedKeywords = mutableListOf<String>()
    private var getCallCount = 0

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers> =
        Result.success(
            FilteredPhotographers(active = emptyList(), inactive = emptyList()),
        )

    override suspend fun getPhotographerMoodKeywords(photographerId: Long): Result<List<String>> =
        if (failsOnGet || (failsOnSecondGet && getCallCount++ > 0)) {
            Result.failure(IllegalStateException("get failed"))
        } else {
            getCallCount++
            Result.success(keywords)
        }

    override suspend fun addPhotoMood(photoMood: String): Result<Unit> =
        if (failsOnAdd) {
            Result.failure(IllegalStateException("add failed"))
        } else {
            addedKeywords.add(photoMood)
            Result.success(Unit)
        }

    override suspend fun deletePhotoMood(photoMood: String): Result<Unit> {
        deletedKeywords.add(photoMood)
        return Result.success(Unit)
    }
}
