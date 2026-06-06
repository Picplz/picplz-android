package com.hm.picplz.data.service

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.mapper.toArea
import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.mapper.toPhotographerInfo
import com.hm.picplz.data.mapper.toReviewData
import com.hm.picplz.data.mapper.toShootingPackage
import com.hm.picplz.data.model.ActiveAreaRequest
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.PhotoMoodRequest
import com.hm.picplz.data.model.PhotographerCameraRequest
import com.hm.picplz.data.model.PhotographerRatingDto
import com.hm.picplz.data.model.PortfolioDto
import com.hm.picplz.data.model.UpdateActiveAreaRequest
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.domain.model.PhotographerReviewData
import com.hm.picplz.domain.model.PhotographerSignup
import com.hm.picplz.domain.model.ShootingPackage
import javax.inject.Inject

@Suppress("TooManyFunctions")
interface PhotographerService {
    suspend fun createPhotographer(signup: PhotographerSignup): AppResult<Unit>

    suspend fun getPhotographerMoodKeywords(photographerId: Long): AppResult<List<String>>

    suspend fun addPhotoMood(photoMood: String): AppResult<Unit>

    suspend fun deletePhotoMood(photoMood: String): AppResult<Unit>

    suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>>

    suspend fun updateActiveAreas(areas: List<Area>): AppResult<List<Area>>

    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<FilteredPhotographers>

    suspend fun getPhotographerInfo(photographerId: Long): AppResult<PhotographerInfo>

    suspend fun getPhotographerRating(photographerId: Long): AppResult<PhotographerRatingDto>

    suspend fun getPhotographerReviews(
        photographerId: Long,
        page: Int = 0,
        size: Int = 10,
        sort: String = "RECOMMENDED",
    ): AppResult<PhotographerReviewData>

    suspend fun getPhotographerProducts(photographerId: Long): AppResult<List<ShootingPackage>>

    suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioDto>
}

class PhotographerServiceImpl
    @Inject
    constructor(
        private val photographerSource: PhotographerSource,
    ) : PhotographerService {
        override suspend fun createPhotographer(signup: PhotographerSignup): AppResult<Unit> =
            photographerSource.createPhotographer(signup.toCreatePhotographerRequest())

        override suspend fun getPhotographerMoodKeywords(photographerId: Long): AppResult<List<String>> =
            photographerSource.getPhotographerInfo(photographerId).map { detail ->
                detail.photoMoods?.mapNotNull { it?.trim() }?.filter(String::isNotEmpty) ?: emptyList()
            }

        override suspend fun addPhotoMood(photoMood: String): AppResult<Unit> =
            photographerSource.addPhotoMood(PhotoMoodRequest(photoMood = photoMood))

        override suspend fun deletePhotoMood(photoMood: String): AppResult<Unit> =
            photographerSource.deletePhotoMood(PhotoMoodRequest(photoMood = photoMood))

        override suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>> =
            photographerSource.getPhotographerInfo(photographerId).map { photographer ->
                photographer
                    .area
                    .orEmpty()
                    .filter { it.code != null && it.name != null }
                    .sortedBy { it.priority ?: Int.MAX_VALUE }
                    .map {
                        Area(
                            id = it.code ?: 0L,
                            name = it.name.orEmpty(),
                            dong = it.name.orEmpty(),
                            ri = null,
                        )
                    }
            }

        override suspend fun updateActiveAreas(areas: List<Area>): AppResult<List<Area>> =
            photographerSource.updateActiveAreas(
                UpdateActiveAreaRequest(
                    areas =
                        areas.mapIndexed { index, area ->
                            ActiveAreaRequest(
                                code = area.id,
                                priority = index + 1,
                            )
                        },
                ),
            ).map { response ->
                response.areas.sortedBy { it.priority }.map { it.toArea() }
            }

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): AppResult<FilteredPhotographers> {
            return photographerSource.getNearbyPhotographers(longitude, latitude, distance).map { cards ->
                val photographers = cards.toDomain()
                FilteredPhotographers(
                    active = photographers.filter { it.isActive },
                    inactive = photographers.filter { !it.isActive },
                )
            }
        }

        override suspend fun getPhotographerInfo(photographerId: Long): AppResult<PhotographerInfo> =
            photographerSource.getPhotographerInfo(photographerId).map { it.toPhotographerInfo() }

        override suspend fun getPhotographerRating(photographerId: Long): AppResult<PhotographerRatingDto> =
            photographerSource.getPhotographerRating(photographerId)

        override suspend fun getPhotographerReviews(
            photographerId: Long,
            page: Int,
            size: Int,
            sort: String,
        ): AppResult<PhotographerReviewData> =
            photographerSource.getPhotographerReviews(photographerId, page, size, sort).map {
                it.toReviewData()
            }

        override suspend fun getPhotographerProducts(photographerId: Long): AppResult<List<ShootingPackage>> =
            photographerSource.getPhotographerProducts(photographerId).map { dtos ->
                dtos.map { it.toShootingPackage() }
            }

        override suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioDto> =
            photographerSource.getPortfolio(portfolioId)
    }

private fun PhotographerSignup.toCreatePhotographerRequest(): CreatePhotographerRequest =
    CreatePhotographerRequest(
        nickname = nickname,
        socialEmail = socialEmail,
        socialProvider = socialProvider,
        socialCode = socialCode,
        profileImage = profileImage,
        photoMoods = photoMoods,
        activeAreas =
            activeAreas.map {
                ActiveAreaRequest(
                    code = it.code,
                    priority = it.priority,
                )
            },
        cameras =
            cameras.map {
                PhotographerCameraRequest(
                    type = it.type.name,
                    brand = it.brand,
                    name = it.name,
                    cameraType = it.cameraType,
                )
            },
    )
