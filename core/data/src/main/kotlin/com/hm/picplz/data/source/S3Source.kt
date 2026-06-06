package com.hm.picplz.data.source

import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.S3Api
import com.hm.picplz.data.model.UploadUrlResponseDto
import com.hm.picplz.data.util.toHttpAppError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

interface S3Source {
    suspend fun getPresignedUploadUrl(
        imageType: String,
        filename: String,
    ): AppResult<UploadUrlResponseDto>

    suspend fun uploadImageToS3(
        uploadUrl: String,
        imageBytes: ByteArray,
        contentType: String,
    ): AppResult<Unit>
}

class S3SourceImpl
    @Inject
    constructor(
        private val s3Api: S3Api,
    ) : S3Source {
        private val plainOkHttpClient = OkHttpClient()

        override suspend fun getPresignedUploadUrl(
            imageType: String,
            filename: String,
        ): AppResult<UploadUrlResponseDto> =
            runCatchingAppError {
                val response = s3Api.getPresignedUploadUrl(imageType, filename)
                if (response.isSuccessful) {
                    response.body()?.data ?: throw AppError.Network.EmptyBody
                } else {
                    throw response.toHttpAppError()
                }
            }

        override suspend fun uploadImageToS3(
            uploadUrl: String,
            imageBytes: ByteArray,
            contentType: String,
        ): AppResult<Unit> =
            runCatchingAppError {
                val requestBody = imageBytes.toRequestBody(contentType.toMediaType())
                val request =
                    Request.Builder()
                        .url(uploadUrl)
                        .header("Content-Type", contentType)
                        .put(requestBody)
                        .build()
                withContext(Dispatchers.IO) {
                    plainOkHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            throw IOException(
                                "S3 upload failed with code: ${response.code}, body: ${response.body?.string()}",
                            )
                        }
                    }
                }
            }
    }
