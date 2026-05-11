package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.CameraCatalog

interface CameraRepository {
    suspend fun getCameraCatalog(): Result<CameraCatalog>
}
