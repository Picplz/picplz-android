package com.hm.picplz.ui.screen.quick_shoot.handler

import com.hm.picplz.ui.screen.quick_shoot.QuickShootIntent
import com.hm.picplz.ui.screen.quick_shoot.QuickShootState

class LocationHandler {
    fun process(
        intent: QuickShootIntent,
        state: QuickShootState,
    ): QuickShootState? {
        return when (intent) {
            is QuickShootIntent.SetAddress -> {
                state.copy(address = intent.address)
            }

            is QuickShootIntent.SetCenterCoords -> {
                state.copy(centerCoords = intent.centerCoords)
            }

            is QuickShootIntent.SetCurrentLocation -> {
                state.copy(
                    userLocation = intent.location,
                    isFetchingGPS = false,
                )
            }

            else -> null
        }
    }
}
