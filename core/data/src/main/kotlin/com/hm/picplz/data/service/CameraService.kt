package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.model.CameraListData
import com.hm.picplz.data.source.CameraSource
import javax.inject.Inject

interface CameraService {
    suspend fun getCameraList(): AppResult<CameraListData>
}

class CameraServiceImpl
    @Inject
    constructor(
        private val cameraSource: CameraSource,
    ) : CameraService {
        override suspend fun getCameraList(): AppResult<CameraListData> = cameraSource.getCameraList()
    }
