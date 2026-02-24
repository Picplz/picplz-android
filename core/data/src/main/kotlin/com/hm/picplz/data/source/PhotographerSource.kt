package com.hm.picplz.data.source

import com.hm.picplz.data.api.PhotographerApi
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.util.safeApiCall
import com.hm.picplz.data.util.safeApiCallUnit
import javax.inject.Inject

interface PhotographerSource {
    suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit>

    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<List<NearbyPhotographerCard>>
}

class PhotographerSourceImpl
    @Inject
    constructor(
        private val photographerApi: PhotographerApi,
    ) : PhotographerSource {
        override suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit> =
            safeApiCallUnit { photographerApi.createPhotographer(request) }

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<List<NearbyPhotographerCard>> =
            safeApiCall { photographerApi.getNearbyPhotographers(longitude, latitude, distance) }
    }
