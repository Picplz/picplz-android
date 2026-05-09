package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.model.ChipItem
import com.hm.picplz.domain.usecase.GetPhotographerMoodKeywordsUseCase
import com.hm.picplz.domain.usecase.UpdatePhotographerMoodKeywordsUseCase
import com.hm.picplz.feature.mypage.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPagePhotographerKeywordEditViewModel
    @Inject
    constructor(
        private val getPhotographerMoodKeywordsUseCase: GetPhotographerMoodKeywordsUseCase,
        private val updatePhotographerMoodKeywordsUseCase: UpdatePhotographerMoodKeywordsUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MyPagePhotographerKeywordEditState.idle())
        val state: StateFlow<MyPagePhotographerKeywordEditState> get() = _state

        private val _sideEffect = Channel<MyPagePhotographerKeywordEditSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: MyPagePhotographerKeywordEditIntent) {
            when (intent) {
                is MyPagePhotographerKeywordEditIntent.LoadKeywords ->
                    loadKeywords(
                        photographerId = intent.photographerId,
                        defaultKeywords = intent.defaultKeywords,
                    )
                is MyPagePhotographerKeywordEditIntent.NavigateToPrev ->
                    sendSideEffect(
                        MyPagePhotographerKeywordEditSideEffect.NavigateToPrev,
                    )
                is MyPagePhotographerKeywordEditIntent.SaveKeywords -> saveKeywords()
                is MyPagePhotographerKeywordEditIntent.AddKeywordChip ->
                    addKeywordChip(intent.label)
                is MyPagePhotographerKeywordEditIntent.DeleteKeywordChip ->
                    deleteKeywordChip(intent.chipId)
                is MyPagePhotographerKeywordEditIntent.UpdateKeywordChip ->
                    updateKeywordChip(
                        intent.chipId,
                        intent.label,
                    )
                is MyPagePhotographerKeywordEditIntent.ToggleKeywordSelection ->
                    updateSelectedKeywordChipList(
                        intent.chipId,
                        intent.label,
                    )
                is MyPagePhotographerKeywordEditIntent.SetEditingChipId ->
                    _state.update {
                        it.copy(editingChipId = intent.chipId)
                    }
                is MyPagePhotographerKeywordEditIntent.DismissToast ->
                    _state.update {
                        it.copy(showToast = false, toastMessageResId = null)
                    }
            }
        }

        private fun loadKeywords(
            photographerId: Long,
            defaultKeywords: List<String>,
        ) {
            if (_state.value.photographerId == photographerId && _state.value.originalKeywords.isNotEmpty()) {
                return
            }

            viewModelScope.launch {
                _state.update {
                    it.copy(
                        photographerId = photographerId,
                        keywordChipList = defaultKeywords.toKeywordChips(),
                        isLoading = true,
                    )
                }
                getPhotographerMoodKeywordsUseCase(photographerId)
                    .onSuccess { keywords -> applyKeywords(keywords, defaultKeywords) }
                    .onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                showToast = true,
                                toastMessageResId = R.string.my_page_keyword_edit_load_failed,
                            )
                        }
                    }
            }
        }

        private fun applyKeywords(
            keywords: List<String>,
            defaultKeywords: List<String> = _state.value.keywordChipList.map { it.label },
        ) {
            val normalizedKeywords = keywords.map(String::trim).filter(String::isNotEmpty)
            val defaultChips = defaultKeywords.toKeywordChips()
            val defaultLabels = defaultChips.map { it.label }.toSet()
            val customChips =
                normalizedKeywords
                    .filterNot { it in defaultLabels }
                    .distinct()
                    .mapIndexed { index, keyword ->
                        ChipItem(id = "custom_${index + 1}", label = keyword, isEditable = true)
                    }
            val chips = defaultChips + customChips
            val selected = chips.filter { chip -> normalizedKeywords.contains(chip.label) }

            _state.update {
                it.copy(
                    keywordChipList = chips,
                    selectedKeywordChipList = selected,
                    originalKeywords = normalizedKeywords,
                    editingChipId = null,
                    isLoading = false,
                    isSaving = false,
                )
            }
        }

        private fun addKeywordChip(label: String) {
            val keyword = label.trim()
            if (keyword.isEmpty()) return
            if (_state.value.keywordChipList.any { it.label == keyword }) {
                showDuplicateToast()
                return
            }

            val newId = nextChipId()
            val newChip = ChipItem(id = newId, label = keyword, isEditable = true)
            _state.update {
                it.copy(
                    keywordChipList = it.keywordChipList + newChip,
                    selectedKeywordChipList = it.selectedKeywordChipList + newChip,
                )
            }
        }

        private fun deleteKeywordChip(chipId: String) {
            _state.update {
                it.copy(
                    keywordChipList = it.keywordChipList.filterNot { chip -> chip.id == chipId },
                    selectedKeywordChipList = it.selectedKeywordChipList.filterNot { chip -> chip.id == chipId },
                )
            }
        }

        private fun updateKeywordChip(
            chipId: String,
            label: String,
        ) {
            val keyword = label.trim()
            if (keyword.isEmpty()) {
                deleteKeywordChip(chipId)
                return
            }
            if (_state.value.keywordChipList.any { it.id != chipId && it.label == keyword }) {
                showDuplicateToast()
                return
            }

            _state.update {
                it.copy(
                    keywordChipList =
                        it.keywordChipList.map { chip ->
                            if (chip.id == chipId) chip.copy(label = keyword) else chip
                        },
                    selectedKeywordChipList =
                        it.selectedKeywordChipList.map { chip ->
                            if (chip.id == chipId) chip.copy(label = keyword) else chip
                        },
                )
            }
        }

        private fun updateSelectedKeywordChipList(
            chipId: String,
            label: String,
        ) {
            _state.update {
                val updatedSelectedChipList =
                    if (it.selectedKeywordChipList.any { chip -> chip.id == chipId }) {
                        it.selectedKeywordChipList.filterNot { chip -> chip.id == chipId }
                    } else {
                        it.selectedKeywordChipList + ChipItem(id = chipId, label = label.trim())
                    }
                it.copy(selectedKeywordChipList = updatedSelectedChipList)
            }
        }

        private fun saveKeywords() {
            val currentState = _state.value
            if (!currentState.hasChanges || currentState.isSaving) return

            viewModelScope.launch {
                _state.update { it.copy(isSaving = true) }
                updatePhotographerMoodKeywordsUseCase(
                    originalKeywords = currentState.originalKeywords,
                    selectedKeywords = currentState.selectedKeywordChipList.map { it.label },
                ).onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    sendSideEffect(MyPagePhotographerKeywordEditSideEffect.NavigateToPrev)
                }.onFailure {
                    getPhotographerMoodKeywordsUseCase(currentState.photographerId)
                        .onSuccess { keywords -> applyKeywords(keywords) }
                        .onFailure { applyKeywords(currentState.originalKeywords) }
                    _state.update {
                        it.copy(
                            isSaving = false,
                            showToast = true,
                            toastMessageResId = R.string.my_page_keyword_edit_save_failed,
                        )
                    }
                }
            }
        }

        private fun nextChipId(): String {
            val maxId =
                _state.value.keywordChipList
                    .mapNotNull { it.id.removePrefix("custom_").toIntOrNull() }
                    .maxOrNull() ?: 0
            return "custom_${maxId + 1}"
        }

        private fun List<String>.toKeywordChips(): List<ChipItem> =
            map(String::trim)
                .filter(String::isNotEmpty)
                .distinct()
                .mapIndexed { index, keyword ->
                    ChipItem(id = (index + 1).toString(), label = keyword)
                }

        private fun showDuplicateToast() {
            _state.update {
                it.copy(
                    showToast = true,
                    toastMessageResId = R.string.my_page_keyword_edit_duplicate,
                )
            }
        }

        private fun sendSideEffect(sideEffect: MyPagePhotographerKeywordEditSideEffect) {
            viewModelScope.launch { _sideEffect.send(sideEffect) }
        }
    }
