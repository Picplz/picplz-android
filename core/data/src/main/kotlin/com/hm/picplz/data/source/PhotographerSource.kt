package com.hm.picplz.data.source

import com.hm.picplz.data.api.PhotographerApi
import com.hm.picplz.data.model.CreatePhotographerRequest
import com.hm.picplz.data.model.NearbyPhotographerCard
import com.hm.picplz.data.model.PhotographerDetailDto
import com.hm.picplz.data.model.PhotographerRatingDto
import com.hm.picplz.data.model.PortfolioDto
import com.hm.picplz.data.model.ProductDto
import com.hm.picplz.data.model.ReviewListDto
import com.hm.picplz.data.util.safeApiCall
import com.hm.picplz.data.util.safeApiCallUnit
import javax.inject.Inject

interface PhotographerSource {
    suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit>

    suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): Result<List<NearbyPhotographerCard>>

    suspend fun getPhotographerInfo(photographerId: Long): Result<PhotographerDetailDto>

    suspend fun getPhotographerRating(photographerId: Long): Result<PhotographerRatingDto>

    suspend fun getPhotographerReviews(
        photographerId: Long,
        page: Int = 0,
        size: Int = 10,
        sort: String = "RECOMMENDED",
    ): Result<ReviewListDto>

    suspend fun getPhotographerProducts(photographerId: Long): Result<List<ProductDto>>

    suspend fun getPortfolio(portfolioId: Long): Result<PortfolioDto>
}

class PhotographerSourceImpl
    @Inject
    constructor(
        private val photographerApi: PhotographerApi,
    ) : PhotographerSource {
        override suspend fun createPhotographer(request: CreatePhotographerRequest): Result<Unit> =
            safeApiCallUnit { photographerApi.createPhotographer(request) }

        override suspend fun getNearbyPhotographers(
            longitude: Double,
            latitude: Double,
            distance: Long,
        ): Result<List<NearbyPhotographerCard>> =
            safeApiCall { photographerApi.getNearbyPhotographers(longitude, latitude, distance) }

        override suspend fun getPhotographerInfo(photographerId: Long): Result<PhotographerDetailDto> =
            safeApiCall { photographerApi.getPhotographerInfo(photographerId) }

        override suspend fun getPhotographerRating(photographerId: Long): Result<PhotographerRatingDto> =
            safeApiCall { photographerApi.getPhotographerRating(photographerId) }

        override suspend fun getPhotographerReviews(
            photographerId: Long,
            page: Int,
            size: Int,
            sort: String,
        ): Result<ReviewListDto> =
            safeApiCall { photographerApi.getPhotographerReviews(photographerId, page, size, sort) }

        override suspend fun getPhotographerProducts(photographerId: Long): Result<List<ProductDto>> =
            safeApiCall { photographerApi.getPhotographerProducts(photographerId) }

        override suspend fun getPortfolio(portfolioId: Long): Result<PortfolioDto> =
            safeApiCall { photographerApi.getPortfolio(portfolioId) }
    }
