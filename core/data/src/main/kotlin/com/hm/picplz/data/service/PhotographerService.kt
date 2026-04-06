package com.hm.picplz.data.service

import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.mapper.toPhotographerInfo
import com.hm.picplz.data.mapper.toReviewData
import com.hm.picplz.data.mapper.toShootingPackage
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.PhotographerInfo
import com.hm.picplz.data.model.PhotographerRatingDto
import com.hm.picplz.data.model.PhotographerReviewData
import com.hm.picplz.data.model.PortfolioDto
import com.hm.picplz.data.model.ShootingPackage
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.domain.model.FilteredPhotographers
import javax.inject.Inject

interface PhotographerService {
    suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit>

    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<FilteredPhotographers>

    suspend fun getPhotographerInfo(photographerId: Long): Result<PhotographerInfo>

    suspend fun getPhotographerRating(photographerId: Long): Result<PhotographerRatingDto>

    suspend fun getPhotographerReviews(
        photographerId: Long,
        page: Int = 0,
        size: Int = 10,
        sort: String = "RECOMMENDED",
    ): Result<PhotographerReviewData>

    suspend fun getPhotographerProducts(photographerId: Long): Result<List<ShootingPackage>>

    suspend fun getPortfolio(portfolioId: Long): Result<PortfolioDto>
}

class PhotographerServiceImpl
    @Inject
    constructor(
        private val photographerSource: PhotographerSource,
    ) : PhotographerService {
        override suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit> =
            photographerSource.createPhotographer(request)

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<FilteredPhotographers> {
            return photographerSource.getNearbyPhotographers(longitude, latitude, distance).map { cards ->
                val photographers = cards.toDomain()
                FilteredPhotographers(
                    active = photographers.filter { it.isActive },
                    inactive = photographers.filter { !it.isActive },
                )
            }
        }

        override suspend fun getPhotographerInfo(photographerId: Long): Result<PhotographerInfo> =
            photographerSource.getPhotographerInfo(photographerId).map { it.toPhotographerInfo() }

        override suspend fun getPhotographerRating(photographerId: Long): Result<PhotographerRatingDto> =
            photographerSource.getPhotographerRating(photographerId)

        override suspend fun getPhotographerReviews(
            photographerId: Long,
            page: Int,
            size: Int,
            sort: String,
        ): Result<PhotographerReviewData> =
            photographerSource.getPhotographerReviews(photographerId, page, size, sort).map {
                it.toReviewData()
            }

        override suspend fun getPhotographerProducts(photographerId: Long): Result<List<ShootingPackage>> =
            photographerSource.getPhotographerProducts(photographerId).map { dtos ->
                dtos.map { it.toShootingPackage() }
            }

        override suspend fun getPortfolio(portfolioId: Long): Result<PortfolioDto> =
            photographerSource.getPortfolio(portfolioId)
    }
