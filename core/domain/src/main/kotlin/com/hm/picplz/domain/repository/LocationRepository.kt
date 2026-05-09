package com.hm.picplz.domain.repository

import com.kakao.vectormap.LatLng

interface LocationRepository {
    fun getCurrentLocation(
        onLocationReceived: (LatLng) -> Unit,
        onPermissionDenied: () -> Unit = {},
    )
}
