package com.hm.picplz.ui.screen.quick_shoot.util

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers

class OffsetGenerator {
    fun generateNonOverlappingOffsets(photographers: FilteredPhotographers): Map<Long, Offset> {
        val allPhotographers = (photographers.active + photographers.inactive).take(MAX_VISIBLE_COUNT)
        return allPhotographers
            .mapIndexed { index, photographer -> photographer.id to PENTAGON_SLOTS[index] }
            .toMap()
    }

    companion object {
        private const val MAX_VISIBLE_COUNT = 5

        /**
         * Fixed pentagon slot positions (dp offsets from center).
         * Slot order (clock-face):
         *   0 = 12 o'clock (top)
         *   1 =  3 o'clock (right)
         *   2 =  5 o'clock (bottom-right)
         *   3 =  7 o'clock (bottom-left)
         *   4 =  9 o'clock (left)
         */
        val PENTAGON_SLOTS =
            listOf(
                Offset(0f, -120f),
                Offset(120f, -30f),
                Offset(80f, 120f),
                Offset(-80f, 120f),
                Offset(-120f, -30f),
            )
    }
}
