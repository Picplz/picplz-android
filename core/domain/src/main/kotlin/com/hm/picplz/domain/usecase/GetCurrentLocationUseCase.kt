package com.hm.picplz.domain.usecase

import com.hm.picplz.domain.repository.LocationRepository
import com.kakao.vectormap.LatLng
import javax.inject.Inject

class GetCurrentLocationUseCase
    @Inject
    constructor(
        private val locationRepository: LocationRepository,
    ) {
        operator fun invoke(
            onLocationReceived: (LatLng) -> Unit,
            onPermissionDenied: () -> Unit = {},
        ) {
            locationRepository.getCurrentLocation(
                onLocationReceived = onLocationReceived,
                onPermissionDenied = onPermissionDenied,
            )
        }
    }
