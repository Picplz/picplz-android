package com.hm.picplz.utils

import com.hm.picplz.R

enum class StarType(val full: Int, val empty: Int) {
    MAIN(R.drawable.star_full, R.drawable.star_empty),
    SUB(R.drawable.small_star_full, R.drawable.small_star_empty)
}

enum class SingleReviewType {
    OVERVIEW, DETAIL
}

object ReviewUtil {
    /**
     * 주어진 평점(totalRating)에 따라 별 아이콘 리스트 반환
     */
    fun calculateStarRating(totalRating: Float, type: StarType = StarType.MAIN): List<Int> {
        val fullStars = totalRating.toInt() // 가득 찬 별 개수
        val hasHalfStar =
            type == StarType.MAIN && (totalRating - fullStars).toDouble() == 0.5 // "MAIN" 타입일 때만 반 별 고려
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0 // 빈 별 개수

        return buildList {
            repeat(fullStars) { add(type.full) }
            if (hasHalfStar) add(R.drawable.star_half) // "SUB"일 경우 추가되지 않음
            repeat(emptyStars) { add(type.empty) }
        }
    }
}