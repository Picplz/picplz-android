package com.hm.picplz.data.api

import com.hm.picplz.data.model.KaKaoAddressResponse
import com.hm.picplz.data.model.KaKaoPlaceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoMapApi {
    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddressFromCoords(
        @Header("Authorization") authorization: String,
        @Query("x") x: String,
        @Query("y") y: String
    ): KaKaoAddressResponse

    @GET("v2/local/search/keyword.json")
    suspend fun getPlaceListByKeyword(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
    ): KaKaoPlaceResponse
}