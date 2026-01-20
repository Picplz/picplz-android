package com.hm.picplz.data.mockdata

import com.hm.picplz.common.model.PhotoPortfolio
import com.hm.picplz.common.model.PhotoReview
import com.hm.picplz.data.model.KeywordBar
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.data.model.PhotographerPortfolio
import com.hm.picplz.data.model.PhotographerReview
import com.hm.picplz.data.model.PhotographerReviewSummary

val mockPhotoReviews =
    listOf(
        PhotoReview(
            reviewId = 0,
            photoReviewUri = "https://picsum.photos/seed/picsum/100",
            index = 0,
        ),
        PhotoReview(
            reviewId = 0,
            photoReviewUri = "https://picsum.photos/seed/picsum1/500/600",
            index = 1,
        ),
        PhotoReview(
            reviewId = 0,
            photoReviewUri = "https://picsum.photos/seed/picsum2/500/600",
            index = 2,
        ),
        PhotoReview(
            reviewId = 1,
            photoReviewUri = "https://picsum.photos/seed/picsum3/500/600",
            index = 0,
        ),
        PhotoReview(
            reviewId = 1,
            photoReviewUri = "https://picsum.photos/seed/picsum4/500/600",
            index = 1,
        ),
        PhotoReview(
            reviewId = 1,
            photoReviewUri = "https://picsum.photos/seed/picsum5/500/600",
            index = 2,
        ),
        PhotoReview(
            reviewId = 1,
            photoReviewUri = "https://picsum.photos/seed/picsum6/500/600",
            index = 3,
        ),
        PhotoReview(
            reviewId = 1,
            photoReviewUri = "https://picsum.photos/seed/picsum7/500/600",
            index = 4,
        ),
        PhotoReview(
            reviewId = 2,
            photoReviewUri = "https://picsum.photos/seed/picsum8/500/600",
            index = 0,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum9/500/600",
            index = 0,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum10/500/600",
            index = 1,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum11/500/600",
            index = 2,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum12/500/600",
            index = 3,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum13/500/600",
            index = 4,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum14/500/600",
            index = 5,
        ),
        PhotoReview(
            reviewId = 4,
            photoReviewUri = "https://picsum.photos/seed/picsum15/500/600",
            index = 6,
        ),
        PhotoReview(
            reviewId = 5,
            photoReviewUri = "https://picsum.photos/seed/picsum16/500/600",
            index = 0,
        ),
        PhotoReview(
            reviewId = 5,
            photoReviewUri = "https://picsum.photos/seed/picsum17/500/600",
            index = 1,
        ),
        PhotoReview(
            reviewId = 7,
            photoReviewUri = "https://picsum.photos/seed/picsum18/500/600",
            index = 0,
        ),
        PhotoReview(
            reviewId = 7,
            photoReviewUri = "https://picsum.photos/seed/picsum19/500/600",
            index = 1,
        ),
        PhotoReview(
            reviewId = 7,
            photoReviewUri = "https://picsum.photos/seed/picsum20/500/600",
            index = 2,
        ),
        PhotoReview(
            reviewId = 9,
            photoReviewUri = "https://picsum.photos/seed/picsum21/500/600",
            index = 0,
        ),
        PhotoReview(
            reviewId = 9,
            photoReviewUri = "https://picsum.photos/seed/picsum22/500/600",
            index = 1,
        ),
    )

val mockReviewSummary =
    PhotographerReviewSummary(
        averageRating = 2.5f,
        keywordBars =
            listOf(
                KeywordBar(
                    label = "사진을 예쁘게 찍어줘요",
                    icon = "https://picsum.photos/seed/icon1/100",
                    count = 14,
                ),
                KeywordBar(label = "포즈 추천을 잘해줘요", icon = "https://picsum.photos/seed/icon2/100", count = 6),
                KeywordBar(label = "친절해요", icon = "https://picsum.photos/seed/icon3/100", count = 2),
                KeywordBar(label = "보정을 잘 해줘요", icon = "https://picsum.photos/seed/icon4/100", count = 30),
            ),
        totalReviewCount = 10,
        totalPhotoReviewCount = 23,
        photoReviews = mockPhotoReviews,
    )

val mockReviews =
    listOf(
        PhotographerReview(
            reviewId = 0,
            profileImageUri = "https://picsum.photos/seed/profile1/100",
            nickname = "사용자1",
            rating = 4.0f,
            createdAt = "2025-02-26",
            isReported = true,
            photoReviews = mockPhotoReviews.slice(0..2),
            photoReviewCount = 3,
            option = "프로필 Only",
            location = "서울 강남",
            reviewText = "하나하나 신경써서 해주시고 잘 알려주세요 사진 처음찍거나 잘 못찍으시는 분들 하시면 후회 안하십니다!",
            isRecommended = true,
            recommendationCount = 3,
        ),
        PhotographerReview(
            reviewId = 1,
            profileImageUri = "https://picsum.photos/seed/profile2/100",
            nickname = "사용자2",
            rating = 3.0f,
            createdAt = "2025-02-25",
            isReported = true,
            photoReviews = mockPhotoReviews.slice(3..7),
            photoReviewCount = 5,
            option = "카카오톡 패키지",
            location = "부산 해운대",
            reviewText = "처음 찍은 사진이 이렇게 예쁘게 나온 건 처음이에요! 진짜 세심하게 신경 써주시고 설명도 잘 해주셔서 감사했어요.",
            isRecommended = false,
            recommendationCount = 1,
        ),
        PhotographerReview(
            reviewId = 2,
            profileImageUri = "https://picsum.photos/seed/profile3/100",
            nickname = "사용자3",
            rating = 3.0f,
            createdAt = "2025-02-25",
            isReported = true,
            photoReviews = mockPhotoReviews.slice(8..8),
            photoReviewCount = 1,
            option = "인스타그램 패키지",
            location = "서울 마포구",
            reviewText = "하나하나 신경 써주신다고 했는데, 생각보다 신경을 덜 써주신 느낌이었어요. 전반적으로 무난한 수준이었지만, 좀 더 세심한 피드백이 필요했어요.",
            isRecommended = false,
            recommendationCount = 0,
        ),
        PhotographerReview(
            reviewId = 3,
            profileImageUri = "https://picsum.photos/seed/profile4/100",
            nickname = "사용자4",
            rating = 1.0f,
            createdAt = "2025-02-25",
            isReported = false,
            photoReviews = emptyList<PhotoReview>(),
            photoReviewCount = 0,
            option = "카카오톡 패키지",
            location = "부산 해운대",
            reviewText = "하나하나 신경 써주신다고 해서 선택했는데, 실제로는 제 기대만큼 세심하지 않았던 것 같아요. 좀 더 디테일한 설명이 필요했어요.",
            isRecommended = true,
            recommendationCount = 10,
        ),
        PhotographerReview(
            reviewId = 4,
            profileImageUri = "https://picsum.photos/seed/profile5/100",
            nickname = "사용자5",
            rating = 5.0f,
            createdAt = "2025-02-25",
            isReported = true,
            photoReviews = mockPhotoReviews.slice(9..15),
            photoReviewCount = 7,
            option = "카카오톡 패키지",
            location = "부산 해운대",
            reviewText = "많이 아쉬웠어요.",
            isRecommended = false,
            recommendationCount = 9,
        ),
        PhotographerReview(
            reviewId = 5,
            profileImageUri = "https://picsum.photos/seed/profile6/100",
            nickname = "사용자6",
            rating = 3.0f,
            createdAt = "2025-02-25",
            isReported = false,
            photoReviews = mockPhotoReviews.slice(16..17),
            photoReviewCount = 2,
            option = "프로필 Only",
            location = "부산 해운대",
            reviewText = "조금 아쉬웠어요.",
            isRecommended = false,
            recommendationCount = 4,
        ),
        PhotographerReview(
            reviewId = 6,
            profileImageUri = "https://picsum.photos/seed/profile7/100",
            nickname = "사용자7",
            rating = 2.0f,
            createdAt = "2025-02-25",
            isReported = false,
            photoReviews = emptyList<PhotoReview>(),
            photoReviewCount = 0,
            option = "인스타그램 패키지",
            location = "부산 해운대",
            reviewText = "사진에 대한 자신감이 없었는데, 덕분에 잘 찍을 수 있었습니다. 한 번 더 배우고 싶은 느낌이에요!",
            isRecommended = true,
            recommendationCount = 5,
        ),
        PhotographerReview(
            reviewId = 7,
            profileImageUri = "https://picsum.photos/seed/profile8/100",
            nickname = "사용자8",
            rating = 1.0f,
            createdAt = "2025-02-25",
            isReported = false,
            photoReviews = mockPhotoReviews.slice(18..20),
            photoReviewCount = 3,
            option = "프로필 Only",
            location = "부산 해운대",
            reviewText = "별로에요.",
            isRecommended = false,
            recommendationCount = 1,
        ),
        PhotographerReview(
            reviewId = 8,
            profileImageUri = "https://picsum.photos/seed/profile9/100",
            nickname = "사용자9",
            rating = 1.0f,
            createdAt = "2025-02-25",
            isReported = false,
            photoReviews = emptyList<PhotoReview>(),
            photoReviewCount = 0,
            option = "프로필 Only",
            location = "부산 해운대",
            reviewText = "잘 못 찍는다고 걱정했는데, 친절하게 하나하나 설명해주시고 도와주셔서 멋진 사진을 얻을 수 있었어요!",
            isRecommended = true,
            recommendationCount = 10,
        ),
        PhotographerReview(
            reviewId = 9,
            profileImageUri = "https://picsum.photos/seed/profile10/100",
            nickname = "사용자10",
            rating = 5.0f,
            createdAt = "2025-02-25",
            isReported = true,
            photoReviews = mockPhotoReviews.slice(21..22),
            photoReviewCount = 2,
            option = "인스타그램 패키지",
            location = "부산 해운대",
            reviewText = "사진 찍는 게 이렇게 쉬울 수 있다니, 처음에는 생각도 못 했어요! 선생님 덕분에 정말 멋진 사진을 남길 수 있었어요!",
            isRecommended = true,
            recommendationCount = 7,
        ),
    )

val mockPhotoPortfolios =
    listOf(
        PhotoPortfolio(
            portfolioId = 0,
            photoPortfolioUri = "https://picsum.photos/seed/port/1080/2640",
            index = 0,
        ),
        PhotoPortfolio(
            portfolioId = 0,
            photoPortfolioUri = "https://picsum.photos/seed/port1/500/600",
            index = 1,
        ),
        PhotoPortfolio(
            portfolioId = 0,
            photoPortfolioUri = "https://picsum.photos/seed/port2/500/600",
            index = 2,
        ),
        PhotoPortfolio(
            portfolioId = 0,
            photoPortfolioUri = "https://picsum.photos/seed/port3/500/600",
            index = 3,
        ),
        PhotoPortfolio(
            portfolioId = 1,
            photoPortfolioUri = "https://picsum.photos/seed/port4/500/600",
            index = 0,
        ),
        PhotoPortfolio(
            portfolioId = 2,
            photoPortfolioUri = "https://picsum.photos/seed/port5/500/600",
            index = 0,
        ),
        PhotoPortfolio(
            portfolioId = 2,
            photoPortfolioUri = "https://picsum.photos/seed/port6/500/600",
            index = 1,
        ),
        PhotoPortfolio(
            portfolioId = 2,
            photoPortfolioUri = "https://picsum.photos/seed/port7/500/600",
            index = 2,
        ),
        PhotoPortfolio(
            portfolioId = 2,
            photoPortfolioUri = "https://picsum.photos/seed/port8/500/600",
            index = 3,
        ),
        PhotoPortfolio(
            portfolioId = 2,
            photoPortfolioUri = "https://picsum.photos/seed/port9/500/600",
            index = 4,
        ),
        PhotoPortfolio(
            portfolioId = 2,
            photoPortfolioUri = "https://picsum.photos/seed/port10/500/600",
            index = 5,
        ),
    )

val mockPortfolios =
    listOf(
        PhotographerPortfolio(
            portfolioId = 0,
            title = "홍익대학교 서울캠퍼스",
            location = "서울 마포구 와우산로",
            createdAt = "2023-01-10",
            photoPortfolios = mockPhotoPortfolios.slice(0..3),
            photoPortfolioCount = 4,
        ),
        PhotographerPortfolio(
            portfolioId = 1,
            title = "강남역 5번출구",
            location = "서울 강남구",
            createdAt = "2023-05-26",
            photoPortfolios = mockPhotoPortfolios.slice(4..4),
            photoPortfolioCount = 1,
        ),
        PhotographerPortfolio(
            portfolioId = 2,
            title = "서울숲",
            location = "서울 까치산로",
            createdAt = "2024-02-01",
            photoPortfolios = mockPhotoPortfolios.slice(5..10),
            photoPortfolioCount = 6,
        ),
    )

val mockPhotographerInfo =
    PhotographerInfo(
        id = 1,
        name = "홍길동",
        socialAccount = "dlwlrma",
        infoText = "10/31 이후 예약 가능합니다. 어쩌고저쩌고 적으면 최대 두 줄까지 적을 수 있습니다",
        isActive = true,
        isFollow = true,
        followCount = 108,
        profileImageUri = "https://picsum.photos/seed/profile/100",
        workingArea = listOf("서울시 마포구", "서울시 용산구"),
        keyword = listOf("#MZ감성", "#을지로감성"),
        photoPortfolios = mockPhotoPortfolios,
    )
