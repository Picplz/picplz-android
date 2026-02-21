package com.hm.picplz.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MemberApi {
    @GET("api/v1/members/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): Response<Unit>
}
