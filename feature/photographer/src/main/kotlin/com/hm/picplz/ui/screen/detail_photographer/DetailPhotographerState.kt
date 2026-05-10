package com.hm.picplz.ui.screen.detail_photographer

import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.domain.model.PhotographerPortfolio
import com.hm.picplz.domain.model.PhotographerReview
import com.hm.picplz.domain.model.PhotographerReviewSummary
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.ui.screen.detail_photographer.review.ReviewSortType

data class DetailPhotographerState(
    val isPreviewMode: Boolean = false,
    val profileInfo: PhotographerInfo = EMPTY_PROFILE,
    val reviewSummary: PhotographerReviewSummary = EMPTY_REVIEW_SUMMARY,
    val reviews: List<PhotographerReview> = emptyList(),
    val portfolios: List<PhotographerPortfolio> = emptyList(),
    val shootingPackages: List<ShootingPackage> = emptyList(),
    val isFollow: Boolean = false,
    val isInfoExpanded: Boolean = false,
    val isAreaExpanded: Boolean = false,
    val isBlocked: Boolean = false,
    val isMenuSheetVisible: Boolean = false,
    val reviewSortType: ReviewSortType = ReviewSortType.LIKES,
    val isSortSheetVisible: Boolean = false,
    val currentReviewIndex: Int = 0,
    val fullScreenImageUri: String? = null,
    val isReportSheetVisible: Boolean = false,
    val previewActionDialog: DetailPreviewAction? = null,
    val toastMessage: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
) {
    companion object {
        private val EMPTY_PROFILE =
            PhotographerInfo(
                id = 0,
                name = "",
                socialAccount = null,
                infoText = "",
                isActive = false,
                isBookable = false,
                isFollow = false,
                followCount = 0,
                profileImageUri = "",
                workingArea = emptyList(),
                keyword = emptyList(),
                equipment = emptyList(),
                photoPortfolios = emptyList(),
            )

        private val EMPTY_REVIEW_SUMMARY =
            PhotographerReviewSummary(
                averageRating = 0f,
                keywordBars = emptyList(),
                totalReviewCount = 0,
                totalPhotoReviewCount = 0,
                photoReviews = emptyList(),
            )

        fun idle(): DetailPhotographerState {
            return DetailPhotographerState()
        }

        fun preview(): DetailPhotographerState {
            return DetailPhotographerState(
                isPreviewMode = true,
                profileInfo = EMPTY_PROFILE,
                reviewSummary = EMPTY_REVIEW_SUMMARY,
                reviews = emptyList(),
                portfolios = emptyList(),
                shootingPackages = emptyList(),
                isLoading = false,
            )
        }

        fun blocked(): DetailPhotographerState {
            return DetailPhotographerState(isBlocked = true, isLoading = false)
        }
    }
}

enum class DetailPreviewAction {
    Booking,
    Follow,
    Block,
    Report,
}
