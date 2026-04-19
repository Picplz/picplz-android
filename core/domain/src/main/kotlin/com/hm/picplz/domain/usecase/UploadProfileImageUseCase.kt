package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.repository.S3Repository
import javax.inject.Inject

class UploadProfileImageUseCase
    @Inject
    constructor(
        private val s3Repository: S3Repository,
    ) {
        suspend operator fun invoke(
            imageBytes: ByteArray,
            filename: String,
        ): Result<String> = s3Repository.uploadProfileImage(imageBytes, filename)
    }
