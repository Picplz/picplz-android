package com.hm.picplz.common.util

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

    fun hasLocationPermissions(context: Context): Boolean {
        return !(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED
        )
    }

    fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }

    fun createLocationPermissionLauncher(
        fragment: Fragment,
        onResult: (Boolean) -> Unit,
    ): ActivityResultLauncher<Array<String>> {
        return fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val allGranted = permissions.all { it.value }
            onResult(allGranted)
        }
    }
}
