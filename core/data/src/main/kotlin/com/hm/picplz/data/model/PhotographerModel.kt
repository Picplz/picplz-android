package com.hm.picplz.data.model

data class CreatePhotographerRequest(
    val nickname: String,
    val socialEmail: String?,
    val socialProvider: String?,
    val socialCode: String?,
    val profileImage: String?,
    val photoMoods: List<String>,
    val activeAreas: List<ActiveAreaRequest>,
    val cameras: List<PhotographerCameraRequest>,
)

data class ActiveAreaRequest(
    val code: Long,
    val priority: Int,
)

data class PhotoMoodRequest(
    val photoMood: String,
)

data class PhotographerCameraRequest(
    val type: String?,
    val brand: String,
    val name: String?,
    val cameraType: String?,
)
