package com.hm.picplz.data.repository

import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class PhotographerRepositoryImpl
    @Inject
    constructor(
        private val photographerService: PhotographerService,
    ) : PhotographerRepository {
        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<FilteredPhotographers> {
            return photographerService.getNearbyPhotographers(longitude, latitude, distance)
        }
    }
