package com.hm.picplz.domain.model

data class PhotographerSignup(
    val nickname: String,
    val socialEmail: String?,
    val socialProvider: String?,
    val socialCode: String,
    val profileImage: String?,
    val photoMoods: List<String>,
    val activeAreas: List<PhotographerSignupActiveArea>,
    val cameras: List<PhotographerSignupCamera>,
)

data class PhotographerSignupActiveArea(
    val code: Long,
    val priority: Int,
)

data class PhotographerSignupCamera(
    val type: PhotographerSignupCameraType,
    val brand: String,
    val name: String?,
    val cameraType: String?,
)

enum class PhotographerSignupCameraType {
    PHONE,
    CAMERA,
}
