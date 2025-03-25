package com.hm.picplz.ui.screen.register_portfolio

import com.hm.picplz.data.model.KaKaoPlaceResponse
import java.time.LocalDate

data class RegisterPortfolioState(
    val isLoading: Boolean = false,
    val placeList: KaKaoPlaceResponse? = null,
    val selectedPlaceName: String = "",
    val selectedDate: LocalDate? = null,
    val placeSearchText: String = ""
) {
    companion object {
        fun idle(): RegisterPortfolioState {
            return RegisterPortfolioState(
            )
        }
    }
}