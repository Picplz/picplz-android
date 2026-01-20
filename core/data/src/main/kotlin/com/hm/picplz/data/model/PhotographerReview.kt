package com.hm.picplz.data.model

import com.hm.picplz.common.model.PhotoPortfolio
import com.hm.picplz.common.model.PhotoReview

data class PhotographerReviewSummary(
    val averageRating: Float, // 총 리뷰 별점 평균
    val keywordBars: List<KeywordBar>, // 키워드 바 구성 요소 리스트
    val totalReviewCount: Int, // 총 리뷰 개수
    val totalPhotoReviewCount: Int, // 총 사진 리뷰 개수

    val photoReviews: List<PhotoReview> // 사진 리뷰 리스트
)

data class KeywordBar(
    val label: String, // 키워드 라벨
    val icon: String, // 키워드 아이콘
    val count: Int // 키워드 선택한 고객 수
)

data class PhotographerReview(
    val reviewId: Int,
    val profileImageUri: String, // 프로필 이미지 URL
    val nickname: String, // 닉네임
    val rating: Float, // 리뷰 별점
    val createdAt: String, // 작성 일자
    val isReported: Boolean, // 신고 여부
    val photoReviews: List<PhotoReview>, // 사진 리스트
    val photoReviewCount: Int,
    // TODO: 사진 옵션 모델 추가하기
    val option: String, // 옵션 (예: "프로필", "카카오", "인스타")
    val location: String, // 촬영지
    val reviewText: String, // 리뷰 내용
    val isRecommended: Boolean, // 추천 여부 (true/false)
    val recommendationCount: Int // 추천 개수
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

//typealias PhotographerReviewSummaryResponse = PhotographerReviewSummaryEntity
//typealias PhotographerReviewResponse = PhotographerReview
//typealias PhotographerReviewListResponse = List<PhotographerReview>