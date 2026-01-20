package com.hm.picplz.ui.util

import com.hm.picplz.core.ui.R

enum class StarType(val full: Int, val empty: Int) {
    MAIN(R.drawable.star_full, R.drawable.star_empty),
    SUB(R.drawable.small_star_full, R.drawable.small_star_empty),
}

enum class SingleReviewType {
    OVERVIEW,
    DETAIL,
}

object ReviewUtil {
    fun calculateStarRating(
        totalRating: Float,
        type: StarType = StarType.MAIN,
    ): List<Int> {
        val fullStars = totalRating.toInt()
        val hasHalfStar = type == StarType.MAIN && (totalRating - fullStars).toDouble() == 0.5
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

        return buildList {
            repeat(fullStars) { add(type.full) }
            if (hasHalfStar) add(R.drawable.star_half)
            repeat(emptyStars) { add(type.empty) }
        }
    }
}
