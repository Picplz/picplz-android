package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerDetail

interface PhotographerRepository {
    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<FilteredPhotographers>

    suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String = "RECOMMENDED",
    ): AppResult<PhotographerDetail>

    suspend fun getPhotographerMoodKeywords(photographerId: Long): AppResult<List<String>>

    suspend fun addPhotoMood(photoMood: String): AppResult<Unit>

    suspend fun deletePhotoMood(photoMood: String): AppResult<Unit>

    suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>>

    suspend fun updateActiveAreas(areas: List<Area>): AppResult<List<Area>>
}
