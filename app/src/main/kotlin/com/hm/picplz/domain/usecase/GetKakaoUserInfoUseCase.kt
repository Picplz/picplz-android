package com.hm.picplz.domain.usecase

import com.hm.picplz.data.model.KakaoUserInfo
import com.hm.picplz.domain.repository.AuthRepository
import javax.inject.Inject

class GetKakaoUserInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<KakaoUserInfo> {
        return authRepository.getKakaoUserInfo()
    }
}
