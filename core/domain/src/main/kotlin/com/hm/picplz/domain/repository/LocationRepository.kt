package com.hm.picplz.domain.repository

import com.hm.picplz.domain.model.LocationCoordinate

interface LocationRepository {
    fun getCurrentLocation(
        onLocationReceived: (LocationCoordinate) -> Unit,
        onPermissionDenied: () -> Unit = {},
    )
}
