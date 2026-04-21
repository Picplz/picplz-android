package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentMemberIdUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        operator fun invoke(): Long? = authRepository.getCurrentMemberId()
    }
