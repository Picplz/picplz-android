package com.hm.picplz.domain.usecase

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.domain.repository.PhotographerRepository
import javax.inject.Inject

class UpdatePhotographerMoodKeywordsUseCase
    @Inject
    constructor(
        private val photographerRepository: PhotographerRepository,
    ) {
        suspend operator fun invoke(
            originalKeywords: List<String>,
            selectedKeywords: List<String>,
        ): AppResult<Unit> =
            runCatchingAppError {
                val original =
                    originalKeywords
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .toSet()
                val selected =
                    selectedKeywords
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .toSet()

                selected.minus(original).forEach { keyword ->
                    photographerRepository.addPhotoMood(keyword).getOrThrow()
                }
                original.minus(selected).forEach { keyword ->
                    photographerRepository.deletePhotoMood(keyword).getOrThrow()
                }
            }
    }
