package com.hm.picplz.data.api

import com.hm.picplz.data.model.UploadUrlResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface S3Api {
    @GET("api/v1/s3/presigned-upload-url")
    suspend fun getPresignedUploadUrl(
        @Query("imageType") imageType: String,
        @Query("filename") filename: String,
    ): UploadUrlResponseDto
}
