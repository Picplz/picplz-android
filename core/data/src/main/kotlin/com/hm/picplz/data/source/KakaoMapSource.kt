package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.KakaoMapApi
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.model.KaKaoAddressResponse
import com.hm.picplz.data.provider.ConfigProvider
import javax.inject.Inject

interface KakaoMapSource {
    suspend fun getAddressFromCoords(request: KaKaoAddressRequest): AppResult<KaKaoAddressResponse>
}

class KakaoMapSourceImpl
    @Inject
    constructor(
        private val kakaoMapApi: KakaoMapApi,
        private val configProvider: ConfigProvider,
    ) : KakaoMapSource {
        override suspend fun getAddressFromCoords(request: KaKaoAddressRequest): AppResult<KaKaoAddressResponse> =
            runCatchingAppError {
                kakaoMapApi.getAddressFromCoords(
                    authorization = "KakaoAK ${configProvider.kakaoRestApiKey}",
                    x = request.x,
                    y = request.y,
                )
            }
    }
