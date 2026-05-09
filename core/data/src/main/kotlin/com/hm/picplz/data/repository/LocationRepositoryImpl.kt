package com.hm.picplz.data.repository

import com.hm.picplz.data.service.LocationService
import com.hm.picplz.domain.repository.LocationRepository
import com.kakao.vectormap.LatLng
import javax.inject.Inject

class LocationRepositoryImpl
    @Inject
    constructor(
        private val locationService: LocationService,
    ) : LocationRepository {
        override fun getCurrentLocation(
            onLocationReceived: (LatLng) -> Unit,
            onPermissionDenied: () -> Unit,
        ) {
            locationService.getCurrentLocation(
                onLocationReceived = onLocationReceived,
                onPermissionDenied = onPermissionDenied,
            )
        }
    }
