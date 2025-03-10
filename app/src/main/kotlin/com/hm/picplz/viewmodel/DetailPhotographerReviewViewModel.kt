package com.hm.picplz.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hm.picplz.ui.screen.detail_photographer.DetailPhotographerReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailPhotographerReviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(DetailPhotographerReviewState.idle())
    val state: StateFlow<DetailPhotographerReviewState> = _state
}