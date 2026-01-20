package com.hm.picplz.ui.screen.search_photographer.util

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.common.util.DisplayMetricsUtil
import com.hm.picplz.domain.model.FilteredPhotographers
import kotlin.random.Random

class OffsetGenerator(private val displayMetricsUtil: DisplayMetricsUtil) {
    fun generateNonOverlappingOffsets(photographers: FilteredPhotographers): Map<Int, Offset> {
        val maxAttempts = 1000

        for (attempt in 1..maxAttempts) {
            try {
                return tryGenerateOffsets(photographers)
            } catch (e: OffsetGenerationException) {
                continue
            }
        }

        throw OffsetGenerationFailedException()
    }

    private class OffsetGenerationException : Exception("개별 위치 생성 실패")

    fun tryGenerateOffsets(photographers: FilteredPhotographers): Map<Int, Offset> {
        val offsets = mutableMapOf<Int, Offset>()
        val minDistance = 110f
        val maxSingleAttempts = 100

        val (maxOffsetX, innerCircleMaxOffsetX, outerCircleMinOffsetX) = displayMetricsUtil.calculateOffsetLimits()
        val center = Offset(0f, 0f)

        fun generateOffset(): Offset {
            return Offset(
                (Random.nextFloat() * 2 - 1) * maxOffsetX,
                (Random.nextFloat() * 2 - 1) * maxOffsetX,
            )
        }

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
                } while (
                    offsets.values.any { existingOffset ->
                        displayMetricsUtil.calculateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < minDistance) ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) > innerCircleMaxOffsetX) ||
                    (index >= 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < outerCircleMinOffsetX)
                )
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
                } while (
                    offsets.values.any { existingOffset ->
                        displayMetricsUtil.calculateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < minDistance) ||
                    (index < 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) > innerCircleMaxOffsetX) ||
                    (index >= 3 && displayMetricsUtil.calculateScreenDistance(center, newOffset) < outerCircleMinOffsetX)
                )
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
                } while (
                    offsets.values.any { existingOffset ->
                        displayMetricsUtil.calculateScreenDistance(existingOffset, newOffset) < minDistance
                    } ||
                    displayMetricsUtil.calculateScreenDistance(center, newOffset) < outerCircleMinOffsetX
                )
                offsets[photographer.id] = newOffset
            }
        }
        return offsets
    }

    class OffsetGenerationFailedException : Exception("전체 위치 생성 최종 실패")
}
