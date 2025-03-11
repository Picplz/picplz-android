package com.hm.picplz.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class PhotoReview(
    val reviewId: Int, // 속한 리뷰 아이디
    val photoReviewUri: String, // 포토 리뷰 URL
    val index: Int, // 속한 리뷰의 포토 중 몇 번째에 해당하는 지
) : Parcelable

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
    val portfolioPhotos: List<String>,
)

//typealias PhotographerReviewSummaryResponse = PhotographerReviewSummaryEntity
//typealias PhotographerReviewResponse = PhotographerReview
//typealias PhotographerReviewListResponse = List<PhotographerReview>