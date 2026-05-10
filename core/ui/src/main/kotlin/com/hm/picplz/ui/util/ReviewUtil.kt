package com.hm.picplz.ui.util

import com.hm.picplz.core.ui.R
import kotlin.math.roundToInt

enum class StarType(val full: Int, val empty: Int) {
    MAIN(R.drawable.star_full, R.drawable.star_empty),
    SUB(R.drawable.small_star_full, R.drawable.small_star_empty),
}

enum class SingleReviewType {
    OVERVIEW,
    DETAIL,
}

object ReviewUtil {
    fun roundToFirstDecimal(rating: Float): Float = (rating * 10).roundToInt() / 10f

    fun calculateStarRating(
        totalRating: Float,
        type: StarType = StarType.MAIN,
    ): List<Int> {
        val normalizedRating =
            if (type == StarType.MAIN) {
                kotlin.math.floor(totalRating * 2f) / 2f
            } else {
                totalRating
            }

        val fullStars = normalizedRating.toInt()
        val hasHalfStar = type == StarType.MAIN && (normalizedRating - fullStars).toDouble() == 0.5
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

        return buildList {
            repeat(fullStars) { add(type.full) }
            if (hasHalfStar) add(R.drawable.star_half)
            repeat(emptyStars) { add(type.empty) }
        }
    }
}
