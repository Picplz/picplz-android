package com.hm.picplz.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    val id: String,
    val nickname: String? = null,
    val email: String? = null,
    val userType: UserType? = null,
    val profileImageUri: String? = null,
    val profileImageObjectKey: String? = null,
    val photographyExperience: PhotographyExperience? = null,
    val photographyVibes: List<String>? = null,
) : Parcelable

@Serializable
enum class UserType {
    User,
    Photographer,
}

enum class SelectionState {
    UNSELECTED,
    SELECTED,
    DESELECTED,
}

@Serializable
enum class PhotographyExperience {
    PHOTO_MAJOR,
    INCOME_GENERATION,
    SNS_OPERATION,
}
