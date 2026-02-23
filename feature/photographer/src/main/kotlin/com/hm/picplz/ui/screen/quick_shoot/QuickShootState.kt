package com.hm.picplz.ui.screen.quick_shoot

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers
import com.kakao.vectormap.LatLng

data class QuickShootState(
    val locationPermissionGranted: Boolean = false,
    val address: String? = null,
    val centerCoords: LatLng = LatLng.from(37.406960, 127.115587),
    val userLocation: LatLng? = null,
    val isFetchingGPS: Boolean = false,
    val isSearchingPhotographer: Boolean = false,
    val nearbyPhotographers: FilteredPhotographers = FilteredPhotographers(),
    val randomOffsets: Map<Long, Offset> = emptyMap(),
    val selectedPhotographerId: Long? = null,
    val centerOffset: Offset? = null,
) {
    companion object {
        fun idle(): QuickShootState {
            return QuickShootState()
        }
    }
}
