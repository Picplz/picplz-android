package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.repository.AuthRepository
import javax.inject.Inject

class UnlinkKakaoUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> {
            return authRepository.unlinkKakao()
        }
    }
