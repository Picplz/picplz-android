package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.FilteredPhotographers
import com.kakao.vectormap.LatLng

interface PhotographerRepository {
    suspend fun getNearbyPhotographers(
        userLocation: LatLng,
        distanceLimit: Int = 2,
        countLimit: Int = 5,
        userAddress: String,
    ): Result<FilteredPhotographers>
}
