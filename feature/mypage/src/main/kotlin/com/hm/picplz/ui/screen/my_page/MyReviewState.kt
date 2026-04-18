package com.hm.picplz.ui.screen.my_page

data class MyReviewState(
    val reviews: List<MyReviewItem> = emptyList(),
    val expandedReviewIds: Set<Int> = emptySet(),
    val pendingDeleteReviewId: Int? = null,
) {
    companion object {
        fun idle() = MyReviewState(reviews = MOCK_REVIEWS)

        private val MOCK_REVIEWS =
            listOf(
                MyReviewItem(
                    id = 1,
                    photographerName = "합정동 불주먹",
                    photographerImageUri = "",
                    rating = 1f,
                    createdAt = "2024.12.03",
                    imageUris =
                        listOf(
                            "https://picsum.photos/seed/my-review-1-1/360/360",
                            "https://picsum.photos/seed/my-review-1-2/360/360",
                        ),
                    option = "남친생기는 프사",
                    location = "서울시 마포구 무대륙",
                    reviewText = "아 개별로에요 다시는 이사람한테 안찍음 ㅡㅡ",
                    likeCount = 4,
                ),
                MyReviewItem(
                    id = 2,
                    photographerName = "합정동 불주먹",
                    photographerImageUri = "",
                    rating = 4f,
                    createdAt = "2024.12.03",
                    imageUris =
                        listOf(
                            "https://picsum.photos/seed/my-review-2-1/360/360",
                            "https://picsum.photos/seed/my-review-2-2/360/360",
                            "https://picsum.photos/seed/my-review-2-3/360/360",
                            "https://picsum.photos/seed/my-review-2-4/360/360",
                        ),
                    option = "남친생기는 프사",
                    location = "서울시 마포구 무대륙",
                    reviewText =
                        "하나하나 신경써서 해주시고 잘 알려주세요 사진 처음찍거나 잘 못찍으시는 분들 하시면 후회 안하십니다. " +
                            "하나하나 디테일까지 봐주시고 포즈도 편하게 잡아주셔서 결과물이 훨씬 만족스러웠어요.",
                    likeCount = 4,
                ),
                MyReviewItem(
                    id = 3,
                    photographerName = "성수동 감성작가",
                    photographerImageUri = "https://picsum.photos/seed/my-review-profile-3/120/120",
                    rating = 3f,
                    createdAt = "2024.11.28",
                    imageUris =
                        listOf(
                            "https://picsum.photos/seed/my-review-3-1/360/360",
                            "https://picsum.photos/seed/my-review-3-2/360/360",
                        ),
                    option = "인스타 피드 촬영",
                    location = "서울시 성동구 서울숲길 17",
                    reviewText =
                        "보정은 깔끔한데 현장에서 설명이 조금 빠른 편이었어요. 원하는 무드가 분명하면 더 잘 맞을 것 같습니다.",
                    likeCount = 2,
                ),
            )
    }
}

data class MyReviewItem(
    val id: Int,
    val photographerName: String,
    val photographerImageUri: String,
    val rating: Float,
    val createdAt: String,
    val imageUris: List<String>,
    val option: String,
    val location: String,
    val reviewText: String,
    val likeCount: Int,
)
