package com.hm.picplz.ui.screen.sign_up.sign_up_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientState
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpClientViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SignUpClientState>(SignUpClientState.idle())
    val state: StateFlow<SignUpClientState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpClientSideEffect>()
    val sideEffect: SharedFlow<SignUpClientSideEffect> get() = _sideEffect

    fun handleIntent(intent: SignUpClientIntent) {
        when (intent) {
            is SetUserInfo -> {}
            is NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpClientSideEffect.NavigateToPrev)
                }
            }
        }
    }
}