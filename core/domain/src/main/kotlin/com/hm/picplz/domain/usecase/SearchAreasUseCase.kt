package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.repository.AreaRepository
import javax.inject.Inject

class SearchAreasUseCase
    @Inject
    constructor(
        private val areaRepository: AreaRepository,
    ) {
        suspend operator fun invoke(keyword: String): AppResult<List<Area>> = areaRepository.searchAreas(keyword)
    }
