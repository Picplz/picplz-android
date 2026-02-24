package com.hm.picplz.ui.screen.quick_shoot.util

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers

class OffsetGenerator {
    fun generateNonOverlappingOffsets(photographers: FilteredPhotographers): Map<Long, Offset> {
        val allPhotographers = (photographers.active + photographers.inactive).take(MAX_VISIBLE_COUNT)
        val slotIndices =
            SLOT_INDICES_BY_COUNT.getOrElse(
                allPhotographers.size,
            ) { SLOT_INDICES_BY_COUNT[MAX_VISIBLE_COUNT]!! }
        return allPhotographers
            .mapIndexed { index, photographer -> photographer.id to CLOCK_SLOTS[slotIndices[index]] }
            .toMap()
    }

    companion object {
        private const val MAX_VISIBLE_COUNT = 5

        /**
         * Fixed clock-face slot positions (dp offsets from center).
         *   0 = 12 o'clock (top)
         *   1 =  3 o'clock (right)
         *   2 =  5 o'clock (bottom-right)
         *   3 =  7 o'clock (bottom-left)
         *   4 =  9 o'clock (left)
         */
        private val CLOCK_SLOTS =
            listOf(
                Offset(0f, -120f),
                Offset(120f, -30f),
                Offset(80f, 120f),
                Offset(-80f, 120f),
                Offset(-120f, -30f),
            )

        private val SLOT_INDICES_BY_COUNT =
            mapOf(
                1 to listOf(0),
                2 to listOf(0, 4),
                3 to listOf(0, 1, 4),
                4 to listOf(0, 1, 3, 4),
                5 to listOf(0, 1, 2, 3, 4),
            )
    }
}
