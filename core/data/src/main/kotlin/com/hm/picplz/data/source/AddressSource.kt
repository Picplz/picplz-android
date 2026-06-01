package com.hm.picplz.data.source

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.AddressApi
import com.hm.picplz.data.model.AreaData
import com.hm.picplz.data.model.AreaNearbyRequest
import com.hm.picplz.data.model.AreaSearchRequest
import javax.inject.Inject

interface AddressSource {
    suspend fun searchArea(request: AreaSearchRequest): AppResult<List<AreaData>>

    suspend fun getNearbyAreas(request: AreaNearbyRequest): AppResult<List<AreaData>>
}

class AddressSourceImpl
    @Inject
    constructor(
        private val addressApi: AddressApi,
    ) : AddressSource {
        override suspend fun searchArea(request: AreaSearchRequest): AppResult<List<AreaData>> =
            runCatchingAppError {
                addressApi.searchAreas(request.keyword).data
            }

        override suspend fun getNearbyAreas(request: AreaNearbyRequest): AppResult<List<AreaData>> =
            runCatchingAppError {
                addressApi.getNearbyAreas(
                    rad = request.rad,
                    lat = request.lat,
                    lng = request.lng,
                ).data
            }
    }
