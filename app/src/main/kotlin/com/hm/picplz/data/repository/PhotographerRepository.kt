package com.hm.picplz.data.repository

import com.hm.picplz.data.service.PhotographerService
import com.hm.picplz.domain.model.FilteredPhotographers
import com.kakao.vectormap.LatLng
import javax.inject.Inject

interface PhotographerRepository {
    suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int = 2,
        countLimit: Int = 5,
        userAddress: String
    ): Result<FilteredPhotographers>
}

class PhotographerRepositoryImpl @Inject constructor(
    private val photographerService: PhotographerService
) : PhotographerRepository {
    override suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int,
        countLimit: Int,
        userAddress: String
    ): Result<FilteredPhotographers> {
        return photographerService.getNearbyPhotographers(
            userLocation,
            distanceLimit,
            countLimit,
            userAddress
        )
    }
}