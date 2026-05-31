package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.UpdateMemberProfileCommand
import com.hm.picplz.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateMemberProfileUseCase
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
    ) {
        suspend operator fun invoke(command: UpdateMemberProfileCommand): AppResult<Unit> =
            memberRepository.updateMemberProfile(command)
    }
