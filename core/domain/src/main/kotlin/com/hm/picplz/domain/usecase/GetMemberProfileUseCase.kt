package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.model.MemberProfile
import com.hm.picplz.domain.repository.MemberRepository
import javax.inject.Inject

class GetMemberProfileUseCase
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
    ) {
        suspend operator fun invoke(memberId: Long): Result<MemberProfile> = memberRepository.getMemberProfile(memberId)
    }
