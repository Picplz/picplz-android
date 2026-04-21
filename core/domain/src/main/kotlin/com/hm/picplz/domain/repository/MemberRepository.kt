package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.model.UpdateMemberProfileCommand

interface MemberRepository {
    suspend fun checkNicknameAvailable(nickname: String): Result<Boolean>

    suspend fun getMemberProfile(memberId: Long): Result<MemberProfile>

    suspend fun updateMemberProfile(command: UpdateMemberProfileCommand): Result<Unit>
}
