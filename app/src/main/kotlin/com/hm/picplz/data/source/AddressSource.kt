package com.hm.picplz.data.source

import com.hm.picplz.BuildConfig
import com.hm.picplz.data.api.KakaoMapApi
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.model.KaKaoAddressResponse
import javax.inject.Inject

interface AddressSource {
    suspend fun getAddressFromCoords(request: KaKaoAddressRequest): Result<KaKaoAddressResponse>
}

class AddressSourceImpl @Inject constructor(
    private val kakaoMapApi: KakaoMapApi
) : AddressSource {
    override suspend fun getAddressFromCoords(request: KaKaoAddressRequest): Result<KaKaoAddressResponse> =
        runCatching {
            kakaoMapApi.getAddressFromCoords(
                authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
                x = request.x,
                y = request.y
            )
        }
}