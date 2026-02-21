package com.hm.picplz.data.source

import com.hm.picplz.data.api.CameraApi
import com.hm.picplz.data.model.CameraListData
import com.hm.picplz.data.model.DeviceBrand
import javax.inject.Inject

interface CameraSource {
    suspend fun getCameraList(): Result<CameraListData>
}

class CameraSourceImpl
    @Inject
    constructor(
        private val cameraApi: CameraApi,
    ) : CameraSource {
        override suspend fun getCameraList(): Result<CameraListData> =
            runCatching {
                val response = cameraApi.getAllCameras()
                if (!response.isSuccessful) {
                    error("Get cameras failed: ${response.code()} ${response.errorBody()?.string()}")
                }
                val cameras = response.body() ?: error("Get cameras failed: empty body")
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
