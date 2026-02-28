package com.hm.picplz.data.service

import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.domain.model.FilteredPhotographers
import javax.inject.Inject

interface PhotographerService {
    suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit>

    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers>
}

class PhotographerServiceImpl
    @Inject
    constructor(
        private val photographerSource: PhotographerSource,
    ) : PhotographerService {
        override suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit> =
            photographerSource.createPhotographer(request)

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<FilteredPhotographers> {
            return photographerSource.getNearbyPhotographers(longitude, latitude, distance).map { cards ->
                val photographers = cards.toDomain()
                FilteredPhotographers(
                    active = photographers.filter { it.isActive },
                    inactive = photographers.filter { !it.isActive },
                )
            }
        }
    }
