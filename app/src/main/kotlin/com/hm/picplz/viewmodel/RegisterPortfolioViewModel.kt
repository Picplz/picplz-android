package com.hm.picplz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.service.AddressService
import com.hm.picplz.ui.screen.register_portfolio.RegisterPortfolioIntent
import com.hm.picplz.ui.screen.register_portfolio.RegisterPortfolioSideEffect
import com.hm.picplz.ui.screen.register_portfolio.RegisterPortfolioState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPortfolioViewModel @Inject constructor(
    private val addressService: AddressService
) : ViewModel() {
    private val TAG = "RegisterPortfolioViewModel"

    private val _state = MutableStateFlow(RegisterPortfolioState.idle())
    val state: StateFlow<RegisterPortfolioState> get() = _state

    private val _sideEffect = MutableSharedFlow<RegisterPortfolioSideEffect>()
    val sideEffect: SharedFlow<RegisterPortfolioSideEffect> get() = _sideEffect

    fun getPlaceByKeyword(query: String) {
        viewModelScope.launch {
            addressService.getPlaceListByKeyword(query)
                .onSuccess { placeList ->
                    Log.d(TAG, placeList.toString())
                }
                .onFailure { error ->
                    Log.e(TAG, "카카오 키워드 장소 검색 실패 : ", error)
                }
        }
    }

    fun handleIntent(intent: RegisterPortfolioIntent) {
        when (intent) {
            is RegisterPortfolioIntent.NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(RegisterPortfolioSideEffect.NavigateToPrev)
                }
            }

            is RegisterPortfolioIntent.SetIsLoading -> {
                _state.update { it.copy(isLoading = intent.value) }
            }

            is RegisterPortfolioIntent.GetPlaceListByKeyword -> {
                handleIntent(RegisterPortfolioIntent.SetIsLoading(true))
                viewModelScope.launch {
                    addressService.getPlaceListByKeyword(intent.keyword)
                        .onSuccess { it ->
                            handleIntent(RegisterPortfolioIntent.SetIsLoading(false))
                            handleIntent(RegisterPortfolioIntent.SetPlaceList(it))
                        }
                        .onFailure { error ->
                            handleIntent(RegisterPortfolioIntent.SetIsLoading(false))
                            Log.e(TAG, "카카오 키워드 장소 검색 실패 : ", error)
                        }
                }
            }

            is RegisterPortfolioIntent.SetPlaceList -> {
                _state.update { it.copy(placeList = intent.placeList) }
            }

            is RegisterPortfolioIntent.SetPlaceName -> {
                _state.update { it.copy(selectedPlaceName = intent.placeName)}
            }

            is RegisterPortfolioIntent.SetDate -> {
                _state.update { it.copy(selectedDate = intent.date) }
            }

            is RegisterPortfolioIntent.SetPlaceSearchText -> {
                _state.update { it.copy(placeSearchText = intent.searchText) }
            }
        }
    }
}