package com.hm.picplz.domain.model

import com.hm.picplz.common.model.UserType

data class MemberProfile(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val introduction: String?,
    val instagram: String?,
    val userType: UserType = UserType.User,
)

data class UpdateMemberProfileCommand(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val introduction: String?,
    val instagram: String?,
)
