package com.hm.picplz.data.repository

import com.hm.picplz.data.service.AddressService
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.repository.AreaRepository
import javax.inject.Inject

class AreaRepositoryImpl
    @Inject
    constructor(
        private val addressService: AddressService,
    ) : AreaRepository {
        override suspend fun searchAreas(keyword: String): Result<List<Area>> = addressService.searchArea(keyword)

        override suspend fun getNearbyAreas(
            rad: Int,
            lat: Double,
            lng: Double,
        ): Result<List<Area>> = addressService.getNearbyAreas(rad, lat, lng)
    }
