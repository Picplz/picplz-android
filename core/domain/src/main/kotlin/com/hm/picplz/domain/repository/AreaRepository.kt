package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.Area

interface AreaRepository {
    suspend fun searchAreas(keyword: String): Result<List<Area>>

    suspend fun getNearbyAreas(
        rad: Int,
        lat: Double,
        lng: Double,
    ): Result<List<Area>>
}
