package com.hm.picplz.data.source

import com.hm.picplz.data.api.AddressApi
import com.hm.picplz.data.model.AreaNearbyRequest
import com.hm.picplz.data.model.AreaNearbyResponse
import com.hm.picplz.data.model.AreaSearchRequest
import com.hm.picplz.data.model.AreaSearchResponse
import javax.inject.Inject

interface AddressSource {
    suspend fun searchArea(request: AreaSearchRequest): Result<AreaSearchResponse>

    suspend fun getNearbyAreas(request: AreaNearbyRequest): Result<AreaNearbyResponse>
}

class AddressSourceImpl
    @Inject
    constructor(
        private val addressApi: AddressApi,
    ) : AddressSource {
        override suspend fun searchArea(request: AreaSearchRequest): Result<AreaSearchResponse> =
            runCatching {
                addressApi.searchAreas(request.keyword)
            }

        override suspend fun getNearbyAreas(request: AreaNearbyRequest): Result<AreaNearbyResponse> =
            runCatching {
                addressApi.getNearbyAreas(
                    rad = request.rad,
                    lat = request.lat,
                    lng = request.lng,
                )
            }
    }
