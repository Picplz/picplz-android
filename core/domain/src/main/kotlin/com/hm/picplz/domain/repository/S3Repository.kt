package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult

interface S3Repository {
    suspend fun uploadProfileImage(
        imageBytes: ByteArray,
        filename: String,
    ): AppResult<String>

    suspend fun uploadProductImage(
        imageBytes: ByteArray,
        filename: String,
    ): AppResult<String>
}
