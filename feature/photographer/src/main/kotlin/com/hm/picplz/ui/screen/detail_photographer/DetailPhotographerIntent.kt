package com.hm.picplz.ui.screen.detail_photographer

sealed interface DetailPhotographerIntent {
    data object NavigateToPrev : DetailPhotographerIntent

    data object ToggleFollow : DetailPhotographerIntent

    data object ToggleInfoExpanded : DetailPhotographerIntent

    data object ToggleBlock : DetailPhotographerIntent
}
