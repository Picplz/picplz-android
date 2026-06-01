package com.hm.picplz.domain.repository

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Area

interface AreaRepository {
    suspend fun searchAreas(keyword: String): AppResult<List<Area>>

    suspend fun getNearbyAreas(
        rad: Int,
        lat: Double,
        lng: Double,
    ): AppResult<List<Area>>
}
