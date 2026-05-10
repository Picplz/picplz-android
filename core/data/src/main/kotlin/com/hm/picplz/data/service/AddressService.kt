package com.hm.picplz.data.service

import com.hm.picplz.data.mapper.toDomain
import com.hm.picplz.data.model.AreaNearbyRequest
import com.hm.picplz.data.model.AreaSearchRequest
import com.hm.picplz.data.source.AddressSource
import com.hm.picplz.domain.model.Area
import javax.inject.Inject

interface AddressService {
    suspend fun searchArea(keyword: String): Result<List<Area>>

    suspend fun getNearbyAreas(
        rad: Int,
        lat: Double,
        lng: Double,
    ): Result<List<Area>>
}

class AddressServiceImpl
    @Inject
    constructor(
        private val addressSource: AddressSource,
    ) : AddressService {
        override suspend fun searchArea(keyword: String): Result<List<Area>> {
            return addressSource.searchArea(
                AreaSearchRequest(keyword = keyword),
            ).map { areas ->
                areas.map { areaData ->
                    areaData.toDomain()
                }
            }
        }

        override suspend fun getNearbyAreas(
            rad: Int,
            lat: Double,
            lng: Double,
        ): Result<List<Area>> {
            return addressSource.getNearbyAreas(
                AreaNearbyRequest(rad = rad, lat = lat, lng = lng),
            ).map { areas ->
                areas.map { areaData ->
                    areaData.toDomain()
                }
            }
        }
    }
