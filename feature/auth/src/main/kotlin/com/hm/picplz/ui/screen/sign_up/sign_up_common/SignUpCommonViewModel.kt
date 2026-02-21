package com.hm.picplz.ui.screen.sign_up.sign_up_common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.data.service.MemberService
import com.hm.picplz.navigation.model.SignUpProfile
import com.hm.picplz.ui.screen.sign_up.sign_up_common.handler.UserInfoHandler
import com.hm.picplz.ui.screen.sign_up.sign_up_common.handler.UserTypeInfoHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpCommonViewModel
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : ViewModel() {
        private val _state = MutableStateFlow<SignUpCommonState>(SignUpCommonState.idle())
        val state: StateFlow<SignUpCommonState> get() = _state

        private val _sideEffect = Channel<SignUpSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private val userTypeInfoHandler = UserTypeInfoHandler()
        private val userInfoHandler = UserInfoHandler()

        fun handleIntent(intent: SignUpCommonIntent) {
            when (intent) {
                is SignUpCommonIntent.NavigateToPrev -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpSideEffect.NavigateToPrev)
                    }
                }

                is SignUpCommonIntent.NavigateToSelected -> {
                    viewModelScope.launch {
                        _state.value.selectedUserType?.let { selectedUserType ->
                            val destination =
                                when (selectedUserType) {
                                    UserType.User -> "sign-up-completion"
                                    UserType.Photographer -> "sign-up-photographer"
                                }

                            val user =
                                User(
                                    id = UUID.randomUUID().toString(),
                                    nickname = _state.value.nickname,
                                    profileImageUri = _state.value.profileImageUri,
                                    userType = _state.value.selectedUserType,
                                )
                            _sideEffect.send(
                                SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected(
                                    destination,
                                    user,
                                ),
                            )
                        }
                    }
                }

                is SignUpCommonIntent.Navigate -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpSideEffect.Navigate(intent.destination))
                    }
                }

                SignUpCommonIntent.CheckNicknameDuplicate -> {
                    if (_state.value.nicknameFieldErrors.isNotEmpty() || _state.value.nickname.isBlank()) {
                        return
                    }

                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                isCheckingNickname = true,
                                isNicknameDuplicate = false,
                            )
                        }

                        memberService.checkNicknameAvailable(_state.value.nickname)
                            .onSuccess { isAvailable ->
                                if (isAvailable) {
                                    _state.update {
                                        it.copy(
                                            isCheckingNickname = false,
                                            isNicknameDuplicate = false,
                                            nicknameFieldErrors =
                                                it.nicknameFieldErrors.filterNot { error ->
                                                    error is NicknameFieldError.DuplicateNickname
                                                },
                                        )
                                    }
                                    _sideEffect.send(SignUpSideEffect.Navigate(SignUpProfile))
                                } else {
                                    _state.update {
                                        it.copy(
                                            isCheckingNickname = false,
                                            isNicknameDuplicate = true,
                                            nicknameFieldErrors =
                                                it.nicknameFieldErrors.filterNot { error ->
                                                    error is NicknameFieldError.DuplicateNickname
                                                } + NicknameFieldError.DuplicateNickname(),
                                        )
                                    }
                                }
                            }
                            .onFailure { error ->
                                _state.update {
                                    it.copy(
                                        isCheckingNickname = false,
                                        error = error,
                                    )
                                }
                            }
                    }
                }

                is SignUpCommonIntent.ShowFileUploadDialog -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpSideEffect.ShowFileUploadDialog)
                    }
                }

                is SignUpCommonIntent.ResetAllSignUpData -> {
                    _state.value = SignUpCommonState.idle()
                }

                else -> {
                    val newState =
                        userTypeInfoHandler.process(intent, _state.value)
                            ?: userInfoHandler.process(intent, _state.value)

                    newState?.let { _state.value = it }
                }
            }
        }
    }
