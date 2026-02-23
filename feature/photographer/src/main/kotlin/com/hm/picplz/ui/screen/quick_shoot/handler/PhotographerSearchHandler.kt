package com.hm.picplz.ui.screen.quick_shoot.handler

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.ui.screen.quick_shoot.QuickShootIntent
import com.hm.picplz.ui.screen.quick_shoot.QuickShootState
import com.hm.picplz.ui.screen.quick_shoot.util.OffsetGenerator

class PhotographerSearchHandler(
    private val offsetGenerator: OffsetGenerator,
) {
    fun process(
        intent: QuickShootIntent,
        state: QuickShootState,
    ): QuickShootState? {
        return when (intent) {
            is QuickShootIntent.SetIsSearchingPhotographer -> {
                state.copy(isSearchingPhotographer = intent.isSearchingPhotographer)
            }

            is QuickShootIntent.SetNearbyPhotographers -> {
                state.copy(nearbyPhotographers = intent.nearbyPhotographers)
            }

            is QuickShootIntent.SetSelectedPhotographerId -> {
                state.copy(
                    selectedPhotographerId =
                        if (state.selectedPhotographerId == intent.photographerId) {
                            null
                        } else {
                            intent.photographerId
                        },
                )
            }

            is QuickShootIntent.CenterSelectedPhotographer -> {
                val newOffset =
                    Offset(
                        x = -intent.offset.x,
                        y = -intent.offset.y,
                    )
                state.copy(centerOffset = newOffset)
            }

            is QuickShootIntent.SetSheetMaxHeight -> {
                state.copy(sheetMaxHeight = intent.maxHeight)
            }

            is QuickShootIntent.SetSheetPeekHeight -> {
                state.copy(sheetPeekHeight = intent.peekHeight)
            }

            is QuickShootIntent.DistributeRandomOffsets -> {
                val randomOffsets = offsetGenerator.generateNonOverlappingOffsets(intent.photographers)
                state.copy(randomOffsets = randomOffsets)
            }

            else -> null
        }
    }
}
