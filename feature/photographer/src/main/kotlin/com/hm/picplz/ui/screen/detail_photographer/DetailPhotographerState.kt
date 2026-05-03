package com.hm.picplz.ui.screen.detail_photographer

import com.hm.picplz.data.mockdata.mockPhotographerInfo
import com.hm.picplz.data.mockdata.mockPortfolios
import com.hm.picplz.data.mockdata.mockReviewSummary
import com.hm.picplz.data.mockdata.mockReviews
import com.hm.picplz.data.mockdata.mockShootingPackages
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.data.model.PhotographerPortfolio
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.data.model.PhotographerReviewSummary
import com.hm.picplz.data.model.ShootingPackage
import com.hm.picplz.ui.screen.detail_photographer.review.ReviewSortType

data class DetailPhotographerState(
    val isPreviewMode: Boolean = false,
    val profileInfo: PhotographerInfo = mockPhotographerInfo,
    val reviewSummary: PhotographerReviewSummary = mockReviewSummary,
    val reviews: List<PhotographerReview> = mockReviews,
    val portfolios: List<PhotographerPortfolio> = mockPortfolios,
    val shootingPackages: List<ShootingPackage> = mockShootingPackages,
    val isFollow: Boolean = mockPhotographerInfo.isFollow,
    val isInfoExpanded: Boolean = false,
    val isAreaExpanded: Boolean = false,
    val isBlocked: Boolean = false,
    val isMenuSheetVisible: Boolean = false,
    val reviewSortType: ReviewSortType = ReviewSortType.LATEST,
    val isSortSheetVisible: Boolean = false,
    val currentReviewIndex: Int = 0,
    val fullScreenImageUri: String? = null,
    val isReportSheetVisible: Boolean = false,
    val previewActionDialog: DetailPreviewAction? = null,
    val toastMessage: String? = null,
) {
    companion object {
        fun idle(): DetailPhotographerState {
            return DetailPhotographerState()
        }

        fun preview(): DetailPhotographerState {
            return DetailPhotographerState(
                isPreviewMode = true,
                profileInfo = mockPhotographerInfo.copy(photoPortfolios = emptyList()),
                reviewSummary =
                    mockReviewSummary.copy(
                        totalReviewCount = 0,
                        totalPhotoReviewCount = 0,
                        photoReviews = emptyList(),
                    ),
                reviews = emptyList(),
                portfolios = emptyList(),
            )
        }

        fun blocked(): DetailPhotographerState {
            return DetailPhotographerState(isBlocked = true)
        }
    }
}

enum class DetailPreviewAction {
    Booking,
    Follow,
    Block,
    Report,
}
