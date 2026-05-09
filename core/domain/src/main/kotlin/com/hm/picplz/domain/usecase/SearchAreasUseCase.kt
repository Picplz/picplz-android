package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.repository.AreaRepository
import javax.inject.Inject

class SearchAreasUseCase
    @Inject
    constructor(
        private val areaRepository: AreaRepository,
    ) {
        suspend operator fun invoke(keyword: String): Result<List<Area>> = areaRepository.searchAreas(keyword)
    }
