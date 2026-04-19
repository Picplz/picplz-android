package com.hm.picplz.data.service

import com.hm.picplz.data.model.MemberInfoResponseDto
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.model.toDomain
import com.hm.picplz.data.source.MemberSource
import com.hm.picplz.domain.model.MemberProfile
import javax.inject.Inject

interface MemberService {
    suspend fun checkNicknameAvailable(nickname: String): Result<Boolean>

    suspend fun getMemberInfo(memberId: Long): Result<MemberProfile>

    suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): Result<Unit>
}

class MemberServiceImpl
    @Inject
    constructor(
        private val memberSource: MemberSource,
    ) : MemberService {
        override suspend fun checkNicknameAvailable(nickname: String): Result<Boolean> =
            memberSource.checkNickname(nickname)

        override suspend fun getMemberInfo(memberId: Long): Result<MemberProfile> =
            memberSource.getMemberInfo(memberId).map(MemberInfoResponseDto::toDomain)

        override suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): Result<Unit> =
            memberSource.updateMemberInfo(request)
    }
