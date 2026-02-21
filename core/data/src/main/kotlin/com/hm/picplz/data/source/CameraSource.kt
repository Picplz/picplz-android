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
                val cameras = cameraApi.getAllCameras()
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
