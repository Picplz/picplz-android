package com.hm.picplz.ui.screen.quick_shoot.util

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.common.util.DisplayMetricsUtil
import com.hm.picplz.domain.model.FilteredPhotographers
import kotlin.random.Random

class OffsetGenerator(private val displayMetricsUtil: DisplayMetricsUtil) {
    fun generateNonOverlappingOffsets(photographers: FilteredPhotographers): Map<Long, Offset> {
        val maxAttempts = 1000

        @Suppress("SwallowedException")
        repeat(maxAttempts) {
            try {
                return tryGenerateOffsets(photographers)
            } catch (_: OffsetGenerationException) {
                // Retry
            }
        }

        throw OffsetGenerationFailedException()
    }

    private class OffsetGenerationException : Exception("개별 위치 생성 실패")

    fun tryGenerateOffsets(photographers: FilteredPhotographers): Map<Long, Offset> {
        val offsets = mutableMapOf<Long, Offset>()
        val minDistance = 110f
        val maxSingleAttempts = 100

        val (maxOffsetX, innerCircleMaxOffsetX, outerCircleMinOffsetX) =
            displayMetricsUtil.calculateOffsetLimits()
        val center = Offset(0f, 0f)

        fun generateOffset(): Offset =
            Offset(
                (Random.nextFloat() * 2 - 1) * maxOffsetX,
                (Random.nextFloat() * 2 - 1) * maxOffsetX,
            )

        fun distanceToCenter(offset: Offset) = displayMetricsUtil.calculateScreenDistance(center, offset)

        fun isTooCloseToExisting(offset: Offset) =
            offsets.values.any { existing ->
                displayMetricsUtil.calculateScreenDistance(existing, offset) < minDistance
            }

        fun isInvalidInnerCircle(
            index: Int,
            offset: Offset,
        ) = index < 3 && (
            distanceToCenter(offset) < minDistance ||
                distanceToCenter(offset) > innerCircleMaxOffsetX
        )

        fun isInvalidOuterCircle(
            index: Int,
            offset: Offset,
        ) = index >= 3 && distanceToCenter(offset) < outerCircleMinOffsetX

        fun isInvalidActiveOffset(
            index: Int,
            offset: Offset,
        ) = isTooCloseToExisting(offset) ||
            isInvalidInnerCircle(index, offset) ||
            isInvalidOuterCircle(index, offset)

        fun isInvalidInactiveOffset(offset: Offset) =
            isTooCloseToExisting(offset) || distanceToCenter(offset) < outerCircleMinOffsetX

        if (photographers.inactive.isEmpty()) {
            photographers.active.forEachIndexed { index, photographer ->
                var newOffset: Offset
                var attempts = 0

                do {
                    attempts++
                    newOffset = generateOffset()

                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (isInvalidActiveOffset(index, newOffset))
                offsets[photographer.id] = newOffset
            }
        } else {
            photographers.active.forEachIndexed { index, photographer ->
                var newOffset: Offset
                var attempts = 0

                do {
                    attempts++
                    newOffset = generateOffset()

                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (isInvalidActiveOffset(index, newOffset))
                offsets[photographer.id] = newOffset
            }
            photographers.inactive.forEach { photographer ->
                var newOffset: Offset
                var attempts = 0

                do {
                    attempts++
                    newOffset = generateOffset()

                    if (attempts >= maxSingleAttempts) {
                        throw OffsetGenerationException()
                    }
                } while (isInvalidInactiveOffset(newOffset))
                offsets[photographer.id] = newOffset
            }
        }
        return offsets
    }

    class OffsetGenerationFailedException : Exception("전체 위치 생성 최종 실패")
}
