package com.hm.picplz.domain.usecase

import android.content.Context
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithKakaoUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(context: Context): AppResult<KaKaoLoginResponse> {
            return authRepository.loginWithKakao(context)
        }

        fun isKakaoTalkAvailable(context: Context): Boolean {
            return authRepository.isKakaoTalkLoginAvailable(context)
        }
    }
