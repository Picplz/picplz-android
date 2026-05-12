package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.domain.model.Area

sealed interface MyPagePhotographerActiveAreaEditIntent {
    data class LoadActiveAreas(val photographerId: Int) : MyPagePhotographerActiveAreaEditIntent

    data object NavigateBack : MyPagePhotographerActiveAreaEditIntent

    data class UpdateSearchQuery(val query: String) : MyPagePhotographerActiveAreaEditIntent

    data object SearchArea : MyPagePhotographerActiveAreaEditIntent

    data class ToggleAreaSelection(val area: Area) : MyPagePhotographerActiveAreaEditIntent

    data class RemoveSelectedArea(val area: Area) : MyPagePhotographerActiveAreaEditIntent

    data object Save : MyPagePhotographerActiveAreaEditIntent

    data object DismissToast : MyPagePhotographerActiveAreaEditIntent
}
