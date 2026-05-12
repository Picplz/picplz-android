package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.domain.model.Area

data class MyPagePhotographerActiveAreaEditState(
    val photographerId: Int = 0,
    val originalAreas: List<Area> = emptyList(),
    val selectedAreas: List<Area> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<Area> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isSaving: Boolean = false,
    val hasSearchCompleted: Boolean = false,
    val toastMessageResId: Int? = null,
) {
    val isSaveEnabled: Boolean
        get() =
            selectedAreas.isNotEmpty() &&
                selectedAreas.map { it.id } != originalAreas.map { it.id } &&
                !isLoading &&
                !isSearching &&
                !isSaving

    val showToast: Boolean
        get() = toastMessageResId != null

    companion object {
        fun idle() = MyPagePhotographerActiveAreaEditState()
    }
}
