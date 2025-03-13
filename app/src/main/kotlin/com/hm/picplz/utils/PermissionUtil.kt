package com.hm.picplz.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

object PermissionUtil {
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    /**
     * 위치 권한이 부여되었는지 확인합니다.
     */
    fun hasLocationPermissions(context: Context): Boolean {
        return !(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)
    }

    /**
     * 위치 권한을 요청합니다.
     */
    fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    /**
     * Fragment에서 사용할 위치 권한 요청 런처를 생성합니다.
     * Fragment 생명주기 초기화 과정에서 호출해야 합니다.
     */
    fun createLocationPermissionLauncher(
        fragment: Fragment,
        onResult: (Boolean) -> Unit
    ): ActivityResultLauncher<Array<String>> {
        return fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.all { it.value }
            onResult(allGranted)
        }
    }
}