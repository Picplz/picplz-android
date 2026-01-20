package com.hm.picplz.data.model

import com.hm.picplz.common.model.PhotoPortfolio
import com.hm.picplz.common.model.PhotoReview

data class PhotographerReviewSummary(
    val averageRating: Float,
    val keywordBars: List<KeywordBar>,
    val totalReviewCount: Int,
    val totalPhotoReviewCount: Int,
    val photoReviews: List<PhotoReview>,
)

data class KeywordBar(
    val label: String,
    val icon: String,
    val count: Int,
)

data class PhotographerReview(
    val reviewId: Int,
    val profileImageUri: String,
    val nickname: String,
    val rating: Float,
    val createdAt: String,
    val isReported: Boolean,
    val photoReviews: List<PhotoReview>,
    val photoReviewCount: Int,
    val option: String,
    val location: String,
    val reviewText: String,
    val isRecommended: Boolean,
    val recommendationCount: Int,
)

data class PhotographerInfo(
    val id: Int,
    val name: String,
    val socialAccount: String?,
    val infoText: String,
    val isActive: Boolean,
    val isFollow: Boolean,
    val followCount: Int,
    val profileImageUri: String,
    val workingArea: List<String>,
    val keyword: List<String>,
    val photoPortfolios: List<PhotoPortfolio>,
)

data class PhotographerPortfolio(
    val portfolioId: Int,
    val title: String,
    val location: String,
    val createdAt: String,
    val photoPortfolios: List<PhotoPortfolio>,
    val photoPortfolioCount: Int,
)

// typealias PhotographerReviewSummaryResponse = PhotographerReviewSummaryEntity
// typealias PhotographerReviewResponse = PhotographerReview
// typealias PhotographerReviewListResponse = List<PhotographerReview>
