package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.FilteredPhotographers

interface PhotographerRepository {
    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers>
}
