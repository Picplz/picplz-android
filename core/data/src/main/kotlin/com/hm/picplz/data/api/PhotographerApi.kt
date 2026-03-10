package com.hm.picplz.data.api

import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.PhotographerRatingDto
import com.hm.picplz.data.model.ReviewListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("api/v1/photographers/{photographerId}/info")
    suspend fun getPhotographerInfo(
        @Path("photographerId") photographerId: Long,
    ): Response<PhotographerDetailDto>

    @GET("api/v1/photographers/{photographerId}/rating")
    suspend fun getPhotographerRating(
        @Path("photographerId") photographerId: Long,
    ): Response<PhotographerRatingDto>

    @GET("api/v1/photographers/{photographerId}/reviews")
    suspend fun getPhotographerReviews(
        @Path("photographerId") photographerId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "RECOMMENDED",
    ): Response<ReviewListDto>
}
