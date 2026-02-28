package com.hm.picplz.data.api

import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PhotographerApi {
    @POST("api/v1/photographers")
    suspend fun createPhotographer(
        @Body request: CreatePhotographerRequest,
    ): Response<Unit>

    @GET("api/v1/members/location/nearby")
    suspend fun getNearbyPhotographers(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("distance") distance: Long,
    ): Response<List<NearbyPhotographerCard>>
}
