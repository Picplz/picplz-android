package com.hm.picplz.data.mapper

import com.hm.picplz.common.model.PhotoReview
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.data.model.PhotographerReviewData
import com.hm.picplz.data.model.PhotographerReviewSummary
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.model.ReviewPhotoDto
import com.hm.picplz.data.model.ReviewSummaryDto
import com.hm.picplz.data.model.ShootingPackage
import com.hm.picplz.domain.model.Photographer

fun NearbyPhotographerCard.toDomain(): Photographer {
    return Photographer(
        id = photographerId,
        name = nickname,
        profileImageUri = profileImage,
        isActive = active == "Y",
        distance = distance,
        photoMoods = photoMoods,
        activeAreas = activeAreas,
        instagram = "@photographer_$photographerId",
        portfolioPhotos = List(4) { "https://picsum.photos/200/200?random=${photographerId * 10 + it}" },
    )
}

fun List<NearbyPhotographerCard>.toDomain(): List<Photographer> {
    return map { it.toDomain() }
}

fun PhotographerDetailDto.toPhotographerInfo(): PhotographerInfo {
    return PhotographerInfo(
        id = photographerId.toInt(),
        name = nickname,
        socialAccount = instagram,
        infoText = introduction ?: "",
        isActive = active == "Y",
        isBookable = active == "Y",
        isFollow = isFollowing == "Y",
        followCount = followers ?: 0,
        profileImageUri = profileImage ?: "",
        workingArea = area?.mapNotNull { it.name } ?: emptyList(),
        keyword = photoMoods ?: emptyList(),
        equipment = emptyList(),
        photoPortfolios = emptyList(),
    )
}

fun ReviewPhotoDto.toPhotoReview(reviewId: Long): PhotoReview {
    return PhotoReview(
        reviewId = reviewId.toInt(),
        photoReviewUri = imageUrl ?: "",
        index = photoOrder ?: 0,
    )
}

fun ReviewSummaryDto.toPhotographerReview(): PhotographerReview {
    val photoReviews = photos?.map { it.toPhotoReview(reviewId) } ?: emptyList()
    return PhotographerReview(
        reviewId = reviewId.toInt(),
        profileImageUri = customerProfileImage ?: "",
        nickname = customerNickname ?: "",
        rating = rating ?: 0f,
        createdAt = createdAt ?: "",
        isReported = false,
        photoReviews = photoReviews,
        photoReviewCount = photoReviews.size,
        option = "",
        location = "",
        reviewText = content ?: "",
        isRecommended = false,
        recommendationCount = 0,
    )
}

fun ReviewListDto.toReviewData(): PhotographerReviewData {
    val mappedReviews = reviews?.map { it.toPhotographerReview() } ?: emptyList()
    val allPhotoReviews =
        mappedReviews.flatMap { it.photoReviews }

    val summary =
        PhotographerReviewSummary(
            averageRating = averageRating ?: 0f,
            keywordBars = emptyList(),
            totalReviewCount = totalReviews?.toInt() ?: 0,
            totalPhotoReviewCount = allPhotoReviews.size,
            photoReviews = allPhotoReviews,
        )

    return PhotographerReviewData(
        summary = summary,
        reviews = mappedReviews,
    )
}

fun ProductDto.toShootingPackage(): ShootingPackage {
    return ShootingPackage(
        packageId = productId.toInt(),
        title = title ?: "",
        price = price ?: 0,
        imageUri = imageUrl ?: "",
        shootingTime = shootingTime ?: "",
        description = description ?: "",
    )
}
