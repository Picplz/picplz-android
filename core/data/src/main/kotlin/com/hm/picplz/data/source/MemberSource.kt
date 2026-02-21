package com.hm.picplz.data.source

import com.hm.picplz.data.api.MemberApi
import javax.inject.Inject

interface MemberSource {
    suspend fun checkNickname(nickname: String): Result<Boolean>
}

class MemberSourceImpl
    @Inject
    constructor(
        private val memberApi: MemberApi,
    ) : MemberSource {
        override suspend fun checkNickname(nickname: String): Result<Boolean> =
            runCatching {
                val response = memberApi.checkNickname(nickname)
                if (response.isSuccessful) {
                    true
                } else {
                    false
                }
            }
    }
