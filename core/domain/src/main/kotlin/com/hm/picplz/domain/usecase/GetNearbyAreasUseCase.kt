package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.repository.AreaRepository
import javax.inject.Inject

class GetNearbyAreasUseCase
    @Inject
    constructor(
        private val areaRepository: AreaRepository,
    ) {
        suspend operator fun invoke(
            rad: Int,
            lat: Double,
            lng: Double,
        ): AppResult<List<Area>> = areaRepository.getNearbyAreas(rad, lat, lng)
    }
