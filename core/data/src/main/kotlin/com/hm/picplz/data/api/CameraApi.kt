package com.hm.picplz.data.api

import com.hm.picplz.data.model.CameraInfoDto
import retrofit2.Response
import retrofit2.http.GET

interface CameraApi {
    @GET("api/v1/cameras")
    suspend fun getAllCameras(): Response<List<CameraInfoDto>>
}
