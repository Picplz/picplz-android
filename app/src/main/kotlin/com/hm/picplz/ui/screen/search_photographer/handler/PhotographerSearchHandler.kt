package com.hm.picplz.ui.screen.search_photographer.handler

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState
import com.hm.picplz.ui.screen.search_photographer.util.OffsetGenerator
import com.hm.picplz.utils.DisplayMetricsUtil

class PhotographerSearchHandler(
    private val displayMetricsUtil: DisplayMetricsUtil,
    private val offsetGenerator: OffsetGenerator
) {
    fun process(intent: SearchPhotographerIntent, state: SearchPhotographerState): SearchPhotographerState? {
        return when (intent) {
            is SearchPhotographerIntent.SetIsSearchingPhotographer -> {
                state.copy(isSearchingPhotographer = intent.isSearchingPhotographer)
            }

            is SearchPhotographerIntent.SetNearbyPhotographers -> {
                state.copy(nearbyPhotographers = intent.nearbyPhotographers)
            }

            is SearchPhotographerIntent.SetSelectedPhotographerId -> {
                state.copy(
                    selectedPhotographerId = if (state.selectedPhotographerId == intent.photographerId) null else intent.photographerId
                )
            }

            is SearchPhotographerIntent.CenterSelectedPhotographer -> {
                val newOffset = Offset(
                    x = -intent.offset.x,
                    y = -intent.offset.y
                )
                state.copy(centerOffset = newOffset)
            }

            is SearchPhotographerIntent.SetSheetMaxHeight -> {
                state.copy(sheetMaxHeight = intent.maxHeight)
            }

            is SearchPhotographerIntent.SetSheetPeekHeight -> {
                state.copy(sheetPeekHeight = intent.peekHeight)
            }

            is SearchPhotographerIntent.DistributeRandomOffsets -> {
                val randomOffsets = offsetGenerator.generateNonOverlappingOffsets(intent.photographers)
                state.copy(randomOffsets = randomOffsets)
            }

            else -> null
        }
    }
}
