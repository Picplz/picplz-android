package com.hm.picplz.data.repository

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
        ): Result<String> =
            s3Service.getUploadUrl("PROFILE", filename)
                .mapCatching { response ->
                    s3Service.uploadImage(response.uploadUrl, imageBytes, "image/jpeg").getOrThrow()
                    response.objectKey
                }

        override suspend fun uploadProductImage(
            imageBytes: ByteArray,
            filename: String,
        ): Result<String> =
            s3Service.getUploadUrl("PORTFOLIO", filename)
                .mapCatching { response ->
                    s3Service.uploadImage(response.uploadUrl, imageBytes, "image/jpeg").getOrThrow()
                    response.objectKey
                }
    }
