package com.hm.picplz.data.api

import com.hm.picplz.data.model.ApiResponse
import com.hm.picplz.data.model.AreaData
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressApi {
    @GET("api/v1/areas/search")
    suspend fun searchAreas(
        @Query("keyword") keyword: String,
    ): ApiResponse<List<AreaData>>

    @GET("api/v1/areas/nearby")
    suspend fun getNearbyAreas(
        @Query("rad") rad: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
    ): ApiResponse<List<AreaData>>
}
