package com.hm.picplz.data.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.service.CameraService
import com.hm.picplz.domain.model.CameraCatalog
import com.hm.picplz.domain.repository.CameraRepository
import javax.inject.Inject

class CameraRepositoryImpl
    @Inject
    constructor(
        private val cameraService: CameraService,
    ) : CameraRepository {
        override suspend fun getCameraCatalog(): AppResult<CameraCatalog> =
            cameraService.getCameraList().map { it.toDomain() }
    }
