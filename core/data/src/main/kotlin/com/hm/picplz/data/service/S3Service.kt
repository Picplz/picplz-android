package com.hm.picplz.data.service

import com.hm.picplz.data.model.UploadUrlResponseDto
import com.hm.picplz.data.source.S3Source
import javax.inject.Inject

interface S3Service {
    suspend fun getUploadUrl(
        imageType: String,
        filename: String,
    ): Result<UploadUrlResponseDto>

    suspend fun uploadImage(
        uploadUrl: String,
        imageBytes: ByteArray,
        contentType: String,
    ): Result<Unit>
}

class S3ServiceImpl
    @Inject
    constructor(
        private val s3Source: S3Source,
    ) : S3Service {
        override suspend fun getUploadUrl(
            imageType: String,
            filename: String,
        ): Result<UploadUrlResponseDto> = s3Source.getPresignedUploadUrl(imageType, filename)

        override suspend fun uploadImage(
            uploadUrl: String,
            imageBytes: ByteArray,
            contentType: String,
        ): Result<Unit> = s3Source.uploadImageToS3(uploadUrl, imageBytes, contentType)
    }
