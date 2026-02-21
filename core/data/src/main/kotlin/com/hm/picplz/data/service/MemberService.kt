package com.hm.picplz.data.service

import com.hm.picplz.data.source.MemberSource
import javax.inject.Inject

interface MemberService {
    suspend fun checkNicknameAvailable(nickname: String): Result<Boolean>
}

class MemberServiceImpl
    @Inject
    constructor(
        private val memberSource: MemberSource,
    ) : MemberService {
        override suspend fun checkNicknameAvailable(nickname: String): Result<Boolean> =
            memberSource.checkNickname(nickname)
    }
