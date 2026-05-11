package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.model.CameraCatalog
import com.hm.picplz.domain.repository.CameraRepository
import javax.inject.Inject

class GetCameraCatalogUseCase
    @Inject
    constructor(
        private val cameraRepository: CameraRepository,
    ) {
        suspend operator fun invoke(): Result<CameraCatalog> = cameraRepository.getCameraCatalog()
    }
