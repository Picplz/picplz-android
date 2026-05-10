package com.hm.picplz.data.source

import com.hm.picplz.data.api.AddressApi
import com.hm.picplz.data.model.AreaData
import com.hm.picplz.data.model.AreaNearbyRequest
import com.hm.picplz.data.model.AreaSearchRequest
import javax.inject.Inject

interface AddressSource {
    suspend fun searchArea(request: AreaSearchRequest): Result<List<AreaData>>

    suspend fun getNearbyAreas(request: AreaNearbyRequest): Result<List<AreaData>>
}

class AddressSourceImpl
    @Inject
    constructor(
        private val addressApi: AddressApi,
    ) : AddressSource {
        override suspend fun searchArea(request: AreaSearchRequest): Result<List<AreaData>> =
            runCatching {
                addressApi.searchAreas(request.keyword)
            }

        override suspend fun getNearbyAreas(request: AreaNearbyRequest): Result<List<AreaData>> =
            runCatching {
                addressApi.getNearbyAreas(
                    rad = request.rad,
                    lat = request.lat,
                    lng = request.lng,
                )
            }
    }
