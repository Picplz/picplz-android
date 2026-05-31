package com.hm.picplz.data.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.service.S3Service
import com.hm.picplz.domain.repository.S3Repository
import javax.inject.Inject

class S3RepositoryImpl
    @Inject
    constructor(
        private val s3Service: S3Service,
    ) : S3Repository {
        override suspend fun uploadProfileImage(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> =
            runCatchingAppError {
                val response = s3Service.getUploadUrl("PROFILE", filename).getOrThrow()
                s3Service.uploadImage(response.uploadUrl, imageBytes, "image/jpeg").getOrThrow()
                response.objectKey
            }

        override suspend fun uploadProductImage(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> =
            runCatchingAppError {
                val response = s3Service.getUploadUrl("PORTFOLIO", filename).getOrThrow()
                s3Service.uploadImage(response.uploadUrl, imageBytes, "image/jpeg").getOrThrow()
                response.objectKey
            }
    }
