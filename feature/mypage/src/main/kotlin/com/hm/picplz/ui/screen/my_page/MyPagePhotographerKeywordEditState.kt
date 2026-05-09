package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.common.model.ChipItem

data class MyPagePhotographerKeywordEditState(
    val photographerId: Long = 0L,
    val keywordChipList: List<ChipItem> = emptyList(),
    val selectedKeywordChipList: List<ChipItem> = emptyList(),
    val originalKeywords: List<String> = emptyList(),
    val editingChipId: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val toastMessageResId: Int? = null,
    val showToast: Boolean = false,
) {
    val hasChanges: Boolean
        get() =
            originalKeywords.normalizedKeywordSet() !=
                selectedKeywordChipList.map { it.label }.normalizedKeywordSet()

    companion object {
        fun idle() = MyPagePhotographerKeywordEditState()
    }
}

internal fun List<String>.normalizedKeywordSet(): Set<String> =
    map(String::trim)
        .filter(String::isNotEmpty)
        .toSet()
