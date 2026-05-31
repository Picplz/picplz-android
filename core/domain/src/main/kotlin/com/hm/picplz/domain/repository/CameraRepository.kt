package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.CameraCatalog

interface CameraRepository {
    suspend fun getCameraCatalog(): AppResult<CameraCatalog>
}
