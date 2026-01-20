package com.hm.picplz.ui.screen.detail_photographer

import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.data.model.PhotographerPortfolio
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.data.model.PhotographerReviewSummary
import com.hm.picplz.data.mockdata.mockPhotographerInfo
import com.hm.picplz.data.mockdata.mockPortfolios
import com.hm.picplz.data.mockdata.mockReviewSummary
import com.hm.picplz.data.mockdata.mockReviews

data class DetailPhotographerState(
    val profileInfo: PhotographerInfo = mockPhotographerInfo,
    val reviewSummary: PhotographerReviewSummary = mockReviewSummary,
    val reviews: List<PhotographerReview> = mockReviews,
    val portfolios: List<PhotographerPortfolio> = mockPortfolios,
) {
    companion object {
        fun idle(): DetailPhotographerState {
            return DetailPhotographerState()
        }
    }
}
