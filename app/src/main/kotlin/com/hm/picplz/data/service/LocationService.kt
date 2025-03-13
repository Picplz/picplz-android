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
            // 권한 없을 경우 콜백 함수 호출 후 return
            onPermissionDenied()
            return
        }

        try {
            val gpsProvider = LocationManager.GPS_PROVIDER
            val networkProvider = LocationManager.NETWORK_PROVIDER

            // GPS 제공자 사용 시도
            if (locationManager!!.isProviderEnabled(gpsProvider)) {
                val locationListener = LocationListener { location ->
                    onLocationReceived(LatLng.from(location.latitude, location.longitude))
                }
                locationListeners.add(locationListener)

                locationManager!!.requestLocationUpdates(
                    gpsProvider,
                    1000L,
                    1.0f,
                    locationListener
                )

                val lastGpsLocation = locationManager?.getLastKnownLocation(gpsProvider)
                if (lastGpsLocation != null) {
                    onLocationReceived(LatLng.from(lastGpsLocation.latitude, lastGpsLocation.longitude))
                }
            }
            // 네트워크 제공자 사용 시도
            else if (locationManager!!.isProviderEnabled(networkProvider)) {
                val locationListener = LocationListener { location ->
                    onLocationReceived(LatLng.from(location.latitude, location.longitude))
                }
                locationListeners.add(locationListener)

                locationManager!!.requestLocationUpdates(
                    networkProvider,
                    1000L,
                    1.0f,
                    locationListener
                )

                val lastNetworkLocation = locationManager?.getLastKnownLocation(networkProvider)
                if (lastNetworkLocation != null) {
                    onLocationReceived(LatLng.from(lastNetworkLocation.latitude, lastNetworkLocation.longitude))
                }
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