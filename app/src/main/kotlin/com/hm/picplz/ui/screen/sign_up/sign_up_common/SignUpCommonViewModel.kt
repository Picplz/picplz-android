package com.hm.picplz.ui.screen.sign_up.sign_up_common

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.User
import com.hm.picplz.data.model.UserType
import com.hm.picplz.ui.screen.sign_up.sign_up_common.handler.UserInfoHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_common.handler.UserTypeInfoHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpCommonViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SignUpCommonState>(SignUpCommonState.idle())
    val state: StateFlow<SignUpCommonState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> get() = _sideEffect

    private val userTypeInfoHandler = UserTypeInfoHandler()
    private val userInfoHandler = UserInfoHandler()

    fun handleIntent(intent: SignUpCommonIntent) {
        when (intent) {
            is SignUpCommonIntent.NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.NavigateToPrev)
                }
            }

            is SignUpCommonIntent.NavigateToSelected -> {
                viewModelScope.launch {
                    _state.value.selectedUserType?.let { selectedUserType ->
                        val destination = when (selectedUserType) {
                            UserType.User -> "sign-up-completion"
                            UserType.Photographer -> "sign-up-photographer"
                        }
                        val userBundle = bundleOf(
                            "userInfo" to User(
                                id = UUID.randomUUID().toString(),
                                nickname = _state.value.nickname,
                                profileImageUri = _state.value.profileImageUri,
                                userType = _state.value.selectedUserType
                            )
                        )
                        _sideEffect.emit(
                            SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected(
                                destination,
                                userBundle
                            )
                        )
                    }
                }
            }

            is SignUpCommonIntent.Navigate -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.Navigate(intent.destination))
                }
            }

            is SignUpCommonIntent.ShowFileUploadDialog -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.ShowFileUploadDialog)
                }
            }

            is SignUpCommonIntent.ResetAllSignUpData -> {
                _state.value = SignUpCommonState.idle()
            }

            else -> {
                val newState = userTypeInfoHandler.process(intent, _state.value)
                    ?: userInfoHandler.process(intent, _state.value)

                newState?.let { _state.value = it }
            }
        }
    }
}
