package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class GetPhotographerActiveAreasUseCase
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
    ) {
        suspend operator fun invoke(photographerId: Long): AppResult<List<Area>> =
            photographerRepository.getActiveAreas(photographerId)
    }
