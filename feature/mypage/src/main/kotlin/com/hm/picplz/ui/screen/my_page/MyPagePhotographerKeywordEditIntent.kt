package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePhotographerKeywordEditIntent {
    data class LoadKeywords(
        val photographerId: Long,
        val defaultKeywords: List<String>,
    ) : MyPagePhotographerKeywordEditIntent

    data object NavigateToPrev : MyPagePhotographerKeywordEditIntent

    data object SaveKeywords : MyPagePhotographerKeywordEditIntent

    data class AddKeywordChip(val label: String) : MyPagePhotographerKeywordEditIntent

    data class DeleteKeywordChip(val chipId: String) : MyPagePhotographerKeywordEditIntent

    data class UpdateKeywordChip(val chipId: String, val label: String) : MyPagePhotographerKeywordEditIntent

    data class ToggleKeywordSelection(
        val chipId: String,
        val label: String,
    ) : MyPagePhotographerKeywordEditIntent

    data class SetEditingChipId(val chipId: String?) : MyPagePhotographerKeywordEditIntent

    data object DismissToast : MyPagePhotographerKeywordEditIntent
}
