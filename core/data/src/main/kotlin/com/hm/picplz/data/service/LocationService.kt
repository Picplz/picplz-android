package com.hm.picplz.data.service

import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import com.hm.picplz.common.util.PermissionUtil
import com.kakao.vectormap.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private var locationManager: LocationManager? = null
        private var currentListener: LocationListener? = null

        fun getCurrentLocation(
            onLocationReceived: (LatLng) -> Unit,
            onPermissionDenied: () -> Unit = {},
        ) {
            if (locationManager == null) {
                locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            }

            val manager =
                locationManager
                    ?: run {
                        onPermissionDenied()
                        return
                    }

            if (!PermissionUtil.hasLocationPermissions(context)) {
                onPermissionDenied()
                return
            }

            cleanup()

            val gpsProvider = LocationManager.GPS_PROVIDER
            val networkProvider = LocationManager.NETWORK_PROVIDER

            when {
                manager.isProviderEnabled(gpsProvider) -> {
                    requestLocationOnce(manager, gpsProvider, onLocationReceived, onPermissionDenied)
                }

                manager.isProviderEnabled(networkProvider) -> {
                    requestLocationOnce(
                        manager,
                        networkProvider,
                        onLocationReceived,
                        onPermissionDenied,
                    )
                }
                else -> onPermissionDenied()
            }
        }

        private fun requestLocationOnce(
            manager: LocationManager,
            provider: String,
            onLocationReceived: (LatLng) -> Unit,
            onPermissionDenied: () -> Unit,
        ) {
            try {
                val lastLocation = manager.getLastKnownLocation(provider)
                if (lastLocation != null) {
                    onLocationReceived(LatLng.from(lastLocation.latitude, lastLocation.longitude))
                    return
                }

                val listener =
                    LocationListener { location ->
                        onLocationReceived(LatLng.from(location.latitude, location.longitude))
                        cleanup()
                    }
                currentListener = listener

                manager.requestLocationUpdates(provider, 0L, 0f, listener)
            } catch (_: SecurityException) {
                onPermissionDenied()
            }
        }

        fun cleanup() {
            currentListener?.let { listener ->
                try {
                    locationManager?.removeUpdates(listener)
                } catch (_: SecurityException) {
                    // Intentionally ignored: cleanup failure is not critical
                }
            }
            currentListener = null
        }
    }
