package com.hm.picplz.utils

import com.hm.picplz.R

object ReviewUtil {
    /**
     * 주어진 평점(reviewNum)에 따라 별 아이콘 리스트 반환
     */
    fun calculateStarRating(reviewNum: Double): List<Int> {
        val fullStars = reviewNum.toInt() // 가득 찬 별 개수
        val hasHalfStar = (reviewNum - fullStars) == 0.5 // 반쪽짜리 별 여부
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0 // 빈 별 개수

        return buildList {
            repeat(fullStars) { add(R.drawable.star_full) }
            if (hasHalfStar) add(R.drawable.star_half)
            repeat(emptyStars) { add(R.drawable.star_empty) }
        }
    }
}