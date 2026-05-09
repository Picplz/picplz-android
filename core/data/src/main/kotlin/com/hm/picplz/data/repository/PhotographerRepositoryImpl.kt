package com.hm.picplz.data.repository

import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerDetail
import com.hm.picplz.domain.repository.PhotographerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PhotographerRepositoryImpl
    @Inject
    constructor(
        private val photographerService: PhotographerService,
    ) : PhotographerRepository {
        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<FilteredPhotographers> = photographerService.getNearbyPhotographers(longitude, latitude, distance)

        override suspend fun getPhotographerDetail(
            photographerId: Long,
            reviewSort: String,
        ): Result<PhotographerDetail> =
            coroutineScope {
                runCatching {
                    val profileDeferred = async { photographerService.getPhotographerInfo(photographerId) }
                    val reviewsDeferred =
                        async {
                            photographerService.getPhotographerReviews(
                                photographerId = photographerId,
                                sort = reviewSort,
                            )
                        }
                    val productsDeferred = async { photographerService.getPhotographerProducts(photographerId) }

                    val profileInfo = profileDeferred.await().getOrThrow()
                    val reviewData = reviewsDeferred.await().getOrThrow()
                    val shootingPackages = productsDeferred.await().getOrThrow()

                    PhotographerDetail(
                        profileInfo = profileInfo,
                        reviewData = reviewData,
                        shootingPackages = shootingPackages,
                    )
                }
            }

        override suspend fun getPhotographerMoodKeywords(photographerId: Long): Result<List<String>> =
            photographerService.getPhotographerMoodKeywords(photographerId)

        override suspend fun addPhotoMood(photoMood: String): Result<Unit> = photographerService.addPhotoMood(photoMood)

        override suspend fun deletePhotoMood(photoMood: String): Result<Unit> =
            photographerService.deletePhotoMood(photoMood)
    }
