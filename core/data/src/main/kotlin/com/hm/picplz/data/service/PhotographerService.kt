package com.hm.picplz.data.service

import com.hm.picplz.common.util.LocationUtil.getDistance
import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.source.PhotographerSource
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.Photographer
import com.kakao.vectormap.LatLng
import javax.inject.Inject

interface PhotographerService {
    suspend fun getPhotographers(): Result<List<Photographer>>
    suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int = 2,
        countLimit: Int = 5,
        userAddress: String
    ): Result<FilteredPhotographers>
}

class PhotographerServiceImpl @Inject constructor(
    private val photographerSource: PhotographerSource
) : PhotographerService {
    override suspend fun getPhotographers(): Result<List<Photographer>> {
        return photographerSource.getPhotographers().map { response ->
            response.map { it.toDomain() }
        }
    }

    override suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int,
        countLimit: Int,
        userAddress: String
    ): Result<FilteredPhotographers> {
        return photographerSource.getPhotographers().map { response ->
            val photographers = response.toDomain()

            val activeNearbyPhotographers = photographers
                .asSequence()
                .filter { it.isActive }
                .mapNotNull {
                    it.location?.let { location ->
                        it to getDistance(userLocation, location)
                    }
                }
                .filter { (_, distance) -> distance <= distanceLimit }
                .sortedBy { (_, distance) -> distance }
                .take(countLimit)
                .map { (photographer, _) -> photographer }
                .toList()

            if (activeNearbyPhotographers.size < countLimit) {
                val inactiveSameAreaPhotographers = photographers
                    .filter { !it.isActive && it.workingArea == userAddress }
                    .shuffled()
                    .take(countLimit - activeNearbyPhotographers.size)

                FilteredPhotographers(
                    active = activeNearbyPhotographers,
                    inactive = inactiveSameAreaPhotographers
                )
            } else {
                FilteredPhotographers(
                    active = activeNearbyPhotographers
                )
            }
        }
    }
}
