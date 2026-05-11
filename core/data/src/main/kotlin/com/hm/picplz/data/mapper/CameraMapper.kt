package com.hm.picplz.data.mapper

import com.hm.picplz.data.model.CameraListData
import com.hm.picplz.domain.model.CameraBrand
import com.hm.picplz.domain.model.CameraCatalog

fun CameraListData.toDomain(): CameraCatalog {
    return CameraCatalog(
        brands = brands.map { brand -> CameraBrand(name = brand.name, models = brand.models) },
        types = types,
    )
}
