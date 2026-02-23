package com.hm.picplz.ui.screen.sign_up.sign_up_common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.data.model.CreateCustomerRequest
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.CustomerService
import com.hm.picplz.data.service.MemberService
import com.hm.picplz.data.service.S3Service
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
        private val customerService: CustomerService,
        private val memberService: MemberService,
        private val s3Service: S3Service,
        private val tokenManager: TokenManager,
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
                        if (_state.value.isSubmitting) {
                            return@launch
                        }
                        _state.value.selectedUserType?.let { selectedUserType ->
                            when (selectedUserType) {
                                UserType.User -> {
                                    _state.update { it.copy(isSubmitting = true, error = null) }

                                    val socialCode = tokenManager.getSocialCode()
                                    if (socialCode == null) {
                                        _state.update {
                                            it.copy(
                                                isSubmitting = false,
                                                error = IllegalStateException("소셜 로그인 정보가 없습니다"),
                                            )
                                        }
                                        return@launch
                                    }

                                    val currentState = _state.value
                                    val request =
                                        CreateCustomerRequest(
                                            nickname = currentState.nickname,
                                            socialEmail = tokenManager.getSocialEmail(),
                                            socialProvider = tokenManager.getSocialProvider(),
                                            socialCode = socialCode,
                                            profileImage = currentState.profileImageObjectKey,
                                        )

                                    customerService.createCustomer(request)
                                        .onSuccess {
                                            _state.update { it.copy(isSubmitting = false) }
                                            sendNavigateToSelectedSideEffect(
                                                destination = "sign-up-completion",
                                            )
                                        }
                                        .onFailure { error ->
                                            _state.update {
                                                it.copy(
                                                    isSubmitting = false,
                                                    error = error,
                                                )
                                            }
                                        }
                                }

                                UserType.Photographer -> {
                                    sendNavigateToSelectedSideEffect(
                                        destination = "sign-up-photographer",
                                    )
                                }
                            }
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
                    _state.update { SignUpCommonState.idle() }
                }

                is SignUpCommonIntent.UploadProfileImage -> {
                    viewModelScope.launch {
                        _state.update { it.copy(isUploadingImage = true) }
                        s3Service.getUploadUrl("PROFILE", intent.filename)
                            .onSuccess { response ->
                                s3Service.uploadImage(response.uploadUrl, intent.imageBytes, "image/jpeg")
                                    .onSuccess {
                                        _state.update {
                                            it.copy(
                                                profileImageObjectKey = response.objectKey,
                                                isUploadingImage = false,
                                            )
                                        }
                                    }
                                    .onFailure { error ->
                                        _state.update {
                                            it.copy(
                                                isUploadingImage = false,
                                                error = error,
                                            )
                                        }
                                    }
                            }
                            .onFailure { error ->
                                _state.update {
                                    it.copy(
                                        isUploadingImage = false,
                                        error = error,
                                    )
                                }
                            }
                    }
                }

                else -> {
                    val newState =
                        userTypeInfoHandler.process(intent, _state.value)
                            ?: userInfoHandler.process(intent, _state.value)

                    _state.update { newState ?: it }
                }
            }
        }

        private suspend fun sendNavigateToSelectedSideEffect(destination: String) {
            val currentState = _state.value
            val user =
                User(
                    id = UUID.randomUUID().toString(),
                    nickname = currentState.nickname,
                    profileImageUri = currentState.profileImageUri,
                    profileImageObjectKey = currentState.profileImageObjectKey,
                    userType = currentState.selectedUserType,
                )
            _sideEffect.send(
                SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected(
                    destination,
                    user,
                ),
            )
        }
    }
