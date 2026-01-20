package com.hm.picplz.navigation

object Routes {
    // Auth
    const val LOGIN = "login"

    // Main
    const val MAIN = "main"
    const val MAIN_SEARCH = "main-search"

    // Sign Up
    const val SIGN_UP = "sign-up"
    const val SIGN_UP_CLIENT = "sign-up-client"
    const val SIGN_UP_PHOTOGRAPHER = "sign-up-photographer"
    const val SIGN_UP_COMPLETION = "sign-up-completion"

    // Photographer
    const val SEARCH_PHOTOGRAPHER = "search-photographer"
    const val PHOTOGRAPHER_MAIN = "photographer-main"
    const val PHOTOGRAPHER_EQUIPMENT_SETTING = "photographer-equipment-setting"

    // Detail Photographer
    const val DETAIL_PHOTOGRAPHER = "detail-photographer"
    const val REVIEW_PHOTOGRAPHER = "review-photographer"
    const val DETAIL_PHOTOGRAPHER_PHOTO_REVIEWS = "detail-photographer-photo-reviews"
    const val DETAIL_PHOTOGRAPHER_PHOTO_PORTFOLIOS = "detail-photographer-photo-portfolios"

    // Chat
    const val CHAT = "chat"

    // My Page
    const val MY_PAGE = "mypage"
    const val MY_PAGE_MODIFY_PROFILE = "mypage-modify-profile"
    const val MY_PAGE_SHOOTING_HISTORY = "mypage-shooting-history"
    const val MY_PAGE_ORDER_SHEET = "mypage-order-sheet"

    // Others
    const val RESERVATION = "reservation"
    const val FEED = "feed"

    const val CHAT_ROOM_PATTERN = "chat/{roomId}"
    const val DETAIL_PHOTOGRAPHER_SINGLE_REVIEW_PATTERN =
        "detail-photographer-single-review/{reviewId}/{photoIndex}"
    const val DETAIL_PHOTOGRAPHER_PORTFOLIOS_PATTERN =
        "detail-photographer-portfolios/{portfolioId}/{photoIndex}"

    fun chatRoom(roomId: String) = "chat/$roomId"

    fun detailPhotographerSingleReview(
        reviewId: Int,
        photoIndex: Int,
    ) = "detail-photographer-single-review/$reviewId/$photoIndex"

    fun detailPhotographerPortfolios(
        portfolioId: Int,
        photoIndex: Int,
    ) = "detail-photographer-portfolios/$portfolioId/$photoIndex"
}
