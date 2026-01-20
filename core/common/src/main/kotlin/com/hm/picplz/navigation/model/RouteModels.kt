package com.hm.picplz.navigation.model

import com.hm.picplz.common.model.User
import kotlinx.serialization.Serializable

// === Graphs ===
@Serializable object AuthGraph

@Serializable object MainGraph

@Serializable object SignUpGraph

// === Auth Screens ===
@Serializable object Login

@Serializable
data class SignUpIntro(val profileImageUri: String? = null)

@Serializable
data class SignUpClient(val userInfo: User)

@Serializable
data class SignUpPhotographer(val userInfo: User)

@Serializable
data class SignUpCompletion(val userInfo: User)

// === Dev ===
@Serializable object Dev

// === Main Screens ===
@Serializable object Main

@Serializable object MainSearch

@Serializable object Feed

@Serializable object Reservation

@Serializable object Chat

@Serializable object MyPage

@Serializable object MyPageModifyProfile

@Serializable object MyPageShootingHistory

@Serializable object MyPageOrderSheet

// === Photographer Screens ===
@Serializable object SearchPhotographer

@Serializable object PhotographerMain

@Serializable object PhotographerEquipmentSetting

// === Detail Screens ===
@Serializable object DetailPhotographer

@Serializable object ReviewPhotographer

@Serializable
object DetailPhotographerPhotoReviews

@Serializable
object DetailPhotographerPhotoPortfolios

// === Screens with Arguments ===
@Serializable
data class ChatRoom(val roomId: String)

@Serializable
data class DetailPhotographerSingleReview(val reviewId: Int, val photoIndex: Int)

@Serializable
data class DetailPhotographerPortfolioDetail(val portfolioId: Int, val photoIndex: Int)
