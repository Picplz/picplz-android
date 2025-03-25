package com.hm.picplz.ui.screen.register_portfolio

import com.hm.picplz.data.model.KaKaoPlaceResponse
import java.time.LocalDate

sealed class RegisterPortfolioIntent {
    data object NavigateToPrev : RegisterPortfolioIntent()
    data class SetIsLoading(val value: Boolean): RegisterPortfolioIntent()
    data class GetPlaceListByKeyword(val keyword: String) : RegisterPortfolioIntent()
    data class SetPlaceList(val placeList: KaKaoPlaceResponse?) : RegisterPortfolioIntent()
    data class SetPlaceName(val placeName: String): RegisterPortfolioIntent()
    data class SetDate(val date: LocalDate): RegisterPortfolioIntent()
    data class SetPlaceSearchText(val searchText: String): RegisterPortfolioIntent()
}