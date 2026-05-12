package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class UpdatePhotographerActiveAreasUseCase
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
    ) {
        suspend operator fun invoke(areas: List<Area>): Result<List<Area>> =
            photographerRepository.updateActiveAreas(areas)
    }
