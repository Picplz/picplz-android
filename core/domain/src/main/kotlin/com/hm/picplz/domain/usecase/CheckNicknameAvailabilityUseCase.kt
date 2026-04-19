package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.repository.MemberRepository
import javax.inject.Inject

class CheckNicknameAvailabilityUseCase
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
    ) {
        suspend operator fun invoke(nickname: String): Result<Boolean> =
            memberRepository.checkNicknameAvailable(nickname)
    }
