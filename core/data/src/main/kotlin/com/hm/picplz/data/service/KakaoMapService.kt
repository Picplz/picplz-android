package com.hm.picplz.data.service

import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.source.KakaoMapSource
import com.kakao.vectormap.LatLng
import javax.inject.Inject

interface KakaoMapService {
    suspend fun getAddressFromCoordinates(coords: LatLng): Result<String>
}

class KakaoMapServiceImpl
    @Inject
    constructor(
        private val kakaoMapSource: KakaoMapSource,
    ) : KakaoMapService {
        override suspend fun getAddressFromCoordinates(coords: LatLng): Result<String> {
            return kakaoMapSource.getAddressFromCoords(
                KaKaoAddressRequest(coords.longitude.toString(), coords.latitude.toString()),
            ).map { response ->
                val twoDepthRegion =
                    response.documents.firstOrNull()?.address?.region_2depth_name
                        ?.split(" ")
                        ?.lastOrNull() ?: ""
                val threeDepthRegion = response.documents.firstOrNull()?.address?.region_3depth_name ?: ""
                "$twoDepthRegion $threeDepthRegion"
            }
        }
    }
