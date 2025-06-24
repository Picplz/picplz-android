package com.hm.picplz.data.model

import com.hm.picplz.ui.model.DeviceCategory

data class DeviceBrand(
    val name: String,
    val models: List<String>
)

object DeviceData {
    val phoneDevices = listOf(
        DeviceBrand("애플", listOf(
            "아이폰 15 Pro Max", "아이폰 15 Pro", "아이폰 15 Plus", "아이폰 15",
            "아이폰 14 Pro Max", "아이폰 14 Pro", "아이폰 14 Plus", "아이폰 14",
            "아이폰 13 Pro Max", "아이폰 13 Pro", "아이폰 13 mini", "아이폰 13",
            "아이폰 12 Pro Max", "아이폰 12 Pro", "아이폰 12 mini", "아이폰 12",
            "아이폰 SE (3세대)",
        )),
        DeviceBrand("삼성", listOf(
            "갤럭시 S24 Ultra", "갤럭시 S24+", "갤럭시 S24",
            "갤럭시 S23 Ultra", "갤럭시 S23+", "갤럭시 S23", "갤럭시 S23 FE",
            "갤럭시 S22 Ultra", "갤럭시 S22+", "갤럭시 S22",
            "갤럭시 S21 Ultra", "갤럭시 S21+", "갤럭시 S21", "갤럭시 S21 FE",
            "갤럭시 노트 20 Ultra", "갤럭시 노트 20",
            "갤럭시 Z 폴드 5", "갤럭시 Z 플립 5",
            "갤럭시 Z 폴드 4", "갤럭시 Z 플립 4",
            "갤럭시 A54", "갤럭시 A34", "갤럭시 A24", "갤럭시 A14",
        )),
    )

    val cameraBrands = listOf(
        "소니", "캐논", "니콘", "후지필름", "파나소닉", "라이카", "올림푸스"
    )

    val cameraTypes = listOf(
        "DSLR 카메라",
        "미러리스 카메라",
        "디지털 카메라",
        "필름 카메라"
    )

    fun getModelsByBrand(category: DeviceCategory, brandName: String): List<String> {
        return when (category) {
            DeviceCategory.PHONE -> phoneDevices
                .find { it.name == brandName }
                ?.models ?: emptyList()
            DeviceCategory.CAMERA -> emptyList()
        }
    }
}