package com.hm.picplz.data.service

import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.model.KaKaoPlaceRequest
import com.hm.picplz.data.model.KaKaoPlaceResponse
import com.hm.picplz.data.source.AddressSource
import com.kakao.vectormap.LatLng
import javax.inject.Inject

interface AddressService {
    suspend fun getAddressFromCoordinates(coords: LatLng): Result<String>
    suspend fun getPlaceListByKeyword(query: String): Result<KaKaoPlaceResponse>  // 추가
}

class AddressServiceImpl @Inject constructor(
    private val addressSource: AddressSource
) : AddressService {
    override suspend fun getAddressFromCoordinates(coords: LatLng): Result<String> {
        return addressSource.getAddressFromCoords(
            KaKaoAddressRequest(coords.longitude.toString(), coords.latitude.toString())
        ).map { response ->
            val twoDepthRegion = response.documents.firstOrNull()?.address?.region_2depth_name
                ?.split(" ")
                ?.lastOrNull() ?: ""
            val threeDepthRegion =
                response.documents.firstOrNull()?.address?.region_3depth_name ?: ""
            "$twoDepthRegion $threeDepthRegion"
        }
    }

    override suspend fun getPlaceListByKeyword(query: String): Result<KaKaoPlaceResponse> {
        return addressSource.getPlaceListByKeyword(
            KaKaoPlaceRequest(query)
        )
    }
}