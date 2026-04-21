package com.hm.picplz.data.source

import com.hm.picplz.data.api.MemberApi
import com.hm.picplz.data.model.MemberInfoResponseDto
import com.hm.picplz.data.model.UpdateMemberInfoRequest
import com.hm.picplz.data.util.safeApiCall
import com.hm.picplz.data.util.safeApiCallUnit
import javax.inject.Inject

interface MemberSource {
    suspend fun checkNickname(nickname: String): Result<Boolean>

    suspend fun getMemberInfo(memberId: Long): Result<MemberInfoResponseDto>

    suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): Result<Unit>
}

class MemberSourceImpl
    @Inject
    constructor(
        private val memberApi: MemberApi,
    ) : MemberSource {
        override suspend fun checkNickname(nickname: String): Result<Boolean> =
            runCatching {
                val response = memberApi.checkNickname(nickname)
                when {
                    response.isSuccessful -> true
                    response.code() == 409 -> false
                    else -> error("Nickname check failed: ${response.code()} ${response.errorBody()?.string()}")
                }
            }

        override suspend fun getMemberInfo(memberId: Long): Result<MemberInfoResponseDto> =
            safeApiCall { memberApi.getMemberInfo(memberId) }

        override suspend fun updateMemberInfo(request: UpdateMemberInfoRequest): Result<Unit> =
            safeApiCallUnit { memberApi.updateMemberInfo(request) }
    }
