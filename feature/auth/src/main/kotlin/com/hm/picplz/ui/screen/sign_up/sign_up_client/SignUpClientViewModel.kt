package com.hm.picplz.ui.screen.sign_up.sign_up_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientIntent.SetUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpClientViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow<SignUpClientState>(SignUpClientState.idle())
        val state: StateFlow<SignUpClientState> get() = _state

        private val _sideEffect = Channel<SignUpClientSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: SignUpClientIntent) {
            when (intent) {
                is SetUserInfo -> {}
                is NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpClientSideEffect.NavigateToPrev)
                    }
                }
            }
        }
    }
