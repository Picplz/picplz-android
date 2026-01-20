package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.handler

import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.ClearSearchResults
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.RemoveSelectedArea
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.ToggleAreaSelection
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateSearchQuery
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState

class AreaSearchHandler {
    fun handleIntent(
        intent: SignUpPhotographerIntent,
        currentState: SignUpPhotographerState,
    ): SignUpPhotographerState? {
        return when (intent) {
            is UpdateSearchQuery -> {
                currentState.copy(
                    searchQuery = intent.query,
                    searchError = null,
                    hasSearchCompleted = false,
                )
            }

            is ClearSearchResults -> {
                currentState.copy(
                    searchResults = emptyList(),
                    searchError = null,
                )
            }

            is ToggleAreaSelection -> {
                val isAlreadySelected =
                    currentState.selectedAreas.any { it.id == intent.area.id }

                if (!isAlreadySelected && currentState.selectedAreas.size >= 10) {
                    return currentState.copy(
                        toastMessage = "활동 지역은 최대 10개까지 선택할 수 있습니다.",
                        showToast = true,
                    )
                }

                val newSelectedAreas =
                    if (isAlreadySelected) {
                        currentState.selectedAreas.filter { it.id != intent.area.id }
                    } else {
                        currentState.selectedAreas + intent.area
                    }

                currentState.copy(selectedAreas = newSelectedAreas)
            }

            is RemoveSelectedArea -> {
                currentState.copy(
                    selectedAreas = currentState.selectedAreas.filter { it.id != intent.area.id },
                )
            }

            else -> null
        }
    }
}
