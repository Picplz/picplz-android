package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.model.UpdateMemberProfileCommand

interface MemberRepository {
    suspend fun checkNicknameAvailable(nickname: String): AppResult<Boolean>

    suspend fun getMemberProfile(memberId: Long): AppResult<MemberProfile>

    suspend fun updateMemberProfile(command: UpdateMemberProfileCommand): AppResult<Unit>
}
