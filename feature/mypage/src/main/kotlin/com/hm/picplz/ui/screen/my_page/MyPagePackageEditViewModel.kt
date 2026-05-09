package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPagePackageEditViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(MyPagePackageEditState.idle())
        val state: StateFlow<MyPagePackageEditState> = _state

        private val _sideEffect = Channel<MyPagePackageEditSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: MyPagePackageEditIntent) {
            when (intent) {
                is MyPagePackageEditIntent.NavigateBack -> sendSideEffect(MyPagePackageEditSideEffect.NavigateBack)
            }
        }

        private fun sendSideEffect(effect: MyPagePackageEditSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }
    }
