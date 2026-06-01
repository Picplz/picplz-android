package com.hm.picplz.data.source

import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.CameraApi
import com.hm.picplz.data.model.CameraListData
import com.hm.picplz.data.model.DeviceBrand
import com.hm.picplz.data.util.toHttpAppError
import javax.inject.Inject

interface CameraSource {
    suspend fun getCameraList(): AppResult<CameraListData>
}

class CameraSourceImpl
    @Inject
    constructor(
        private val cameraApi: CameraApi,
    ) : CameraSource {
        override suspend fun getCameraList(): AppResult<CameraListData> =
            runCatchingAppError {
                val response = cameraApi.getAllCameras()
                if (!response.isSuccessful) {
                    throw response.toHttpAppError()
                }
                val cameras = response.body()?.data ?: throw AppError.Network.EmptyBody
                val brands =
                    cameras
                        .groupBy { it.brand }
                        .map { (brand, items) ->
                            DeviceBrand(
                                name = brand,
                                models = items.map { it.name },
                            )
                        }
                val types = cameras.map { it.type }.distinct()
                CameraListData(brands = brands, types = types)
            }
    }
