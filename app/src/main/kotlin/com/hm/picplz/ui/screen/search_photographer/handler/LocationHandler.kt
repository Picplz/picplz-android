package com.hm.picplz.ui.screen.search_photographer.handler

import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState

class LocationHandler {
    fun process(intent: SearchPhotographerIntent, state: SearchPhotographerState): SearchPhotographerState? {
        return when (intent) {
            is SearchPhotographerIntent.SetAddress -> {
                state.copy(address = intent.address)
            }

            is SearchPhotographerIntent.SetCenterCoords -> {
                state.copy(centerCoords = intent.centerCoords)
            }

            is SearchPhotographerIntent.SetCurrentLocation -> {
                state.copy(
                    userLocation = intent.location,
                    isFetchingGPS = false
                )
            }

            else -> null
        }
    }
}
