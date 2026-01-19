package com.hm.picplz.ui.screen.search_photographer

sealed interface SearchPhotographerSideEffect {
    data object NavigateToPrev : SearchPhotographerSideEffect
    data object RequestLocationPermission : SearchPhotographerSideEffect
}