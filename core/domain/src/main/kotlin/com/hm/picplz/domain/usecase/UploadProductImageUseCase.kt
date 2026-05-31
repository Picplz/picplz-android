package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.repository.S3Repository
import javax.inject.Inject

class UploadProductImageUseCase
    @Inject
    constructor(
        private val s3Repository: S3Repository,
    ) {
        suspend operator fun invoke(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> = s3Repository.uploadProductImage(imageBytes, filename)
    }
