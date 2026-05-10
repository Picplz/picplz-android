package com.hm.picplz.data.repository

import com.hm.picplz.data.service.LocationService
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl
    @Inject
    constructor(
        private val locationService: LocationService,
    ) : LocationRepository {
        override fun getCurrentLocation(
            onLocationReceived: (LocationCoordinate) -> Unit,
            onPermissionDenied: () -> Unit,
        ) {
            locationService.getCurrentLocation(
                onLocationReceived = { location ->
                    onLocationReceived(
                        LocationCoordinate(
                            latitude = location.latitude,
                            longitude = location.longitude,
                        ),
                    )
                },
                onPermissionDenied = onPermissionDenied,
            )
        }
    }
