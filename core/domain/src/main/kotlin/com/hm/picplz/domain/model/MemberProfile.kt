package com.hm.picplz.domain.model

data class MemberProfile(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val introduction: String?,
    val instagram: String?,
)

data class UpdateMemberProfileCommand(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val introduction: String?,
    val instagram: String?,
)
