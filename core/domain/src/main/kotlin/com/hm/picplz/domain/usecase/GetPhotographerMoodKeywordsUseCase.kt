package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class GetPhotographerMoodKeywordsUseCase
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
    ) {
        suspend operator fun invoke(photographerId: Long): Result<List<String>> =
            photographerRepository.getPhotographerMoodKeywords(photographerId)
    }
