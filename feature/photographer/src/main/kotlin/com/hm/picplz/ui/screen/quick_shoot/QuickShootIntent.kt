package com.hm.picplz.ui.screen.quick_shoot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.hm.picplz.domain.model.FilteredPhotographers
import com.kakao.vectormap.LatLng

sealed interface QuickShootIntent {
    data object NavigateToPrev : QuickShootIntent

    data class SetAddress(val address: String) : QuickShootIntent

    data class GetAddress(val coords: LatLng) : QuickShootIntent

    data class SetCenterCoords(val centerCoords: LatLng) : QuickShootIntent

    data class SetCurrentLocation(val location: LatLng) : QuickShootIntent

    data class RequestLocationPermission(val unit: Unit = Unit) : QuickShootIntent

    data class SetLocationPermissionGranted(val granted: Boolean) : QuickShootIntent

    data object GetCurrentLocation : QuickShootIntent

    data class SetIsSearchingPhotographer(val isSearchingPhotographer: Boolean) : QuickShootIntent

    data class SetNearbyPhotographers(val nearbyPhotographers: FilteredPhotographers) : QuickShootIntent

    data object FetchNearbyPhotographers : QuickShootIntent

    data object RefetchNearbyPhotographers : QuickShootIntent

    data class DistributeRandomOffsets(val photographers: FilteredPhotographers) : QuickShootIntent

    data class SetSelectedPhotographerId(val photographerId: Int?) : QuickShootIntent

    data class SetSheetMaxHeight(val maxHeight: Dp) : QuickShootIntent

    data class SetSheetPeekHeight(val peekHeight: Dp?) : QuickShootIntent

    data class CenterSelectedPhotographer(val offset: Offset) : QuickShootIntent
}
