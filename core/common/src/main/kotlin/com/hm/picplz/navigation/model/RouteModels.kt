package com.hm.picplz.navigation.model

import com.hm.picplz.common.model.User
import kotlinx.serialization.Serializable

sealed interface NavigationRoute

// === Graphs ===
@Serializable object AuthGraph : NavigationRoute

@Serializable object MainGraph : NavigationRoute

@Serializable object SignUpGraph : NavigationRoute

// === Auth Screens ===
@Serializable object Login : NavigationRoute

@Serializable
data class SignUpIntro(val profileImageUri: String? = null) : NavigationRoute

@Serializable
data class SignUpClient(val userInfo: User) : NavigationRoute

@Serializable
data class SignUpPhotographer(val userInfo: User) : NavigationRoute

@Serializable
data class SignUpCompletion(val userInfo: User) : NavigationRoute

// === SignUp Common Internal Routes ===
@Serializable object SignUpSelectType : NavigationRoute

@Serializable object SignUpNickname : NavigationRoute

@Serializable object SignUpProfile : NavigationRoute

// === SignUp Photographer Internal Routes ===
@Serializable object SignUpMainLocation : NavigationRoute

@Serializable object SignUpExperience : NavigationRoute

@Serializable object SignUpDetailExperience : NavigationRoute

@Serializable object SignUpPhotographyVibe : NavigationRoute

@Serializable object SignUpCareerPeriod : NavigationRoute

@Serializable object SignUpDevice : NavigationRoute

@Serializable
data class SignUpAddDevice(val category: String = "phone") : NavigationRoute

// === Dev ===
@Serializable object Dev : NavigationRoute

// === Main Screens ===
@Serializable object Main : NavigationRoute

@Serializable object MainSearch : NavigationRoute

@Serializable object Feed : NavigationRoute

@Serializable object Reservation : NavigationRoute

@Serializable object Chat : NavigationRoute

@Serializable object MyPage : NavigationRoute

@Serializable object MyPageModifyProfile : NavigationRoute

@Serializable object MyPageShootingHistory : NavigationRoute

@Serializable object MyPageOrderSheet : NavigationRoute

// === Photographer Screens ===
@Serializable object SearchPhotographer : NavigationRoute

@Serializable object PhotographerMain : NavigationRoute

@Serializable object PhotographerEquipmentSetting : NavigationRoute

// === Detail Screens ===
@Serializable
data class DetailPhotographer(val photographerId: Int) : NavigationRoute

@Serializable
data class ReviewPhotographer(val photographerId: Int) : NavigationRoute

@Serializable
data class DetailPhotographerPhotoReviews(val photographerId: Int) : NavigationRoute

@Serializable
data class DetailPhotographerPhotoPortfolios(val photographerId: Int) : NavigationRoute

// === Screens with Arguments ===
@Serializable
data class ChatRoom(val roomId: String) : NavigationRoute

@Serializable
data class DetailPhotographerSingleReview(val reviewId: Int, val photoIndex: Int) : NavigationRoute

@Serializable
data class DetailPhotographerPortfolioDetail(val portfolioId: Int, val photoIndex: Int) : NavigationRoute

// === Reservation Screens ===
@Serializable
data object DetailReservation : NavigationRoute
