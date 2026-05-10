package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerDetail

interface PhotographerRepository {
    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers>

    suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String = "RECOMMENDED",
    ): Result<PhotographerDetail>
}
