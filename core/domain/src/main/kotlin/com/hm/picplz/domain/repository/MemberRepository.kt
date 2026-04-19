package com.hm.picplz.domain.repository

interface MemberRepository {
    suspend fun checkNicknameAvailable(nickname: String): Result<Boolean>
}
