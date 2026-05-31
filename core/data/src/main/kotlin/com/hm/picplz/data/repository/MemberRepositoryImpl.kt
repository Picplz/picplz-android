package com.hm.picplz.data.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.toRequest
import com.hm.picplz.data.service.MemberService
import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.model.UpdateMemberProfileCommand
import com.hm.picplz.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : MemberRepository {
        override suspend fun checkNicknameAvailable(nickname: String): AppResult<Boolean> =
            memberService.checkNicknameAvailable(nickname)

        override suspend fun getMemberProfile(memberId: Long): AppResult<MemberProfile> =
            memberService.getMemberInfo(memberId)

        override suspend fun updateMemberProfile(command: UpdateMemberProfileCommand): AppResult<Unit> =
            memberService.updateMemberInfo(command.toRequest())
    }
