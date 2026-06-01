package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.PhotographerDetail
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class GetPhotographerDetailUseCase
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
    ) {
        suspend operator fun invoke(
            photographerId: Long,
            reviewSort: String = "RECOMMENDED",
        ): AppResult<PhotographerDetail> = photographerRepository.getPhotographerDetail(photographerId, reviewSort)
    }
