package com.hm.picplz.domain.repository

interface S3Repository {
    suspend fun uploadProfileImage(
        imageBytes: ByteArray,
        filename: String,
    ): Result<String>
}
