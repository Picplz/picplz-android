package com.hm.picplz.data.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
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
        context: Context,
        onLocationReceived: (LatLng) -> Unit
    ) {
        // LocationManager 초기화
        if (locationManager == null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        // 권한 체크
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val gpsProvider = LocationManager.GPS_PROVIDER
        val networkProvider = LocationManager.NETWORK_PROVIDER

        // GPS 제공자 사용 시도
        if (locationManager!!.isProviderEnabled(gpsProvider)) {
            // 위치 업데이트 리스너 등록
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

            // 마지막 알려진 위치 확인
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

            // 마지막 알려진 위치 확인
            val lastNetworkLocation = locationManager?.getLastKnownLocation(networkProvider)
            if (lastNetworkLocation != null) {
                onLocationReceived(LatLng.from(lastNetworkLocation.latitude, lastNetworkLocation.longitude))
            }
        }
    }

    fun cleanup() {
        locationListeners.forEach { listener ->
            locationManager?.removeUpdates(listener)
        }
        locationListeners.clear()
    }
}