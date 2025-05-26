package com.hm.picplz.data.api

import com.hm.picplz.data.model.AreaSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressApi {
    @GET("api/v1/areas/search")
    suspend fun searchAreas(
        @Query("keyword") keyword: String
    ): AreaSearchResponse
}