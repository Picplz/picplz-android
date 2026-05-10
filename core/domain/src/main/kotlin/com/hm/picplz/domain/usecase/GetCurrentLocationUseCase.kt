package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase
    @Inject
    constructor(
        private val locationRepository: LocationRepository,
    ) {
        operator fun invoke(
            onLocationReceived: (LocationCoordinate) -> Unit,
            onPermissionDenied: () -> Unit = {},
        ) {
            locationRepository.getCurrentLocation(
                onLocationReceived = onLocationReceived,
                onPermissionDenied = onPermissionDenied,
            )
        }
    }
