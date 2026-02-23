package com.hm.picplz.data.api

import com.hm.picplz.data.model.CreatePhotographerRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PhotographerApi {
    @POST("api/v1/photographers")
    suspend fun createPhotographer(
        @Body request: CreatePhotographerRequest,
    ): Response<Unit>
}
