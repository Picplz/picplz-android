package com.hm.picplz.data.service

import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import com.hm.picplz.utils.PermissionUtil
import com.kakao.vectormap.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var locationManager: LocationManager? = null
    private val locationListeners = mutableListOf<LocationListener>()

    fun getCurrentLocation(
        onLocationReceived: (LatLng) -> Unit,
        onPermissionDenied: () -> Unit = {}
    ) {
        // LocationManager 초기화
        if (locationManager == null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        // 권한 체크
        if (!PermissionUtil.hasLocationPermissions(context)) {
            onPermissionDenied()
            return
        }

        val gpsProvider = LocationManager.GPS_PROVIDER
        val networkProvider = LocationManager.NETWORK_PROVIDER

        if (locationManager!!.isProviderEnabled(gpsProvider)) {
            requestLocationUpdates(gpsProvider, onLocationReceived, onPermissionDenied)
        } else if (locationManager!!.isProviderEnabled(networkProvider)) {
            requestLocationUpdates(networkProvider, onLocationReceived, onPermissionDenied)
        }
    }

    private fun requestLocationUpdates(
        provider: String,
        onLocationReceived: (LatLng) -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        try {
            val locationListener = LocationListener { location ->
                onLocationReceived(LatLng.from(location.latitude, location.longitude))
            }
            locationListeners.add(locationListener)

            locationManager!!.requestLocationUpdates(
                provider,
                1000L,
                1.0f,
                locationListener
            )

            val lastLocation = locationManager?.getLastKnownLocation(provider)
            if (lastLocation != null) {
                onLocationReceived(LatLng.from(lastLocation.latitude, lastLocation.longitude))
            }
        } catch (securityException: SecurityException) {
            onPermissionDenied()
        }
    }

    fun cleanup() {
        locationListeners.forEach { listener ->
            try {
                locationManager?.removeUpdates(listener)
            } catch (_: SecurityException) {}
        }
        locationListeners.clear()
    }
}