package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.CustomerService
import com.hm.picplz.data.service.MemberService
import com.hm.picplz.data.service.S3Service
import com.hm.picplz.domain.model.CustomerSignup
import com.hm.picplz.domain.usecase.LoginWithKakaoUseCase
import com.hm.picplz.feature.auth.R
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
        private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
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

                        if (_state.value.isUploadingImage) {
                            _sideEffect.send(SignUpSideEffect.ShowToast(R.string.sign_up_profile_image_wait_upload))
                            return@launch
                        }

                        if (
                            _state.value.profileImageUri != null &&
                            _state.value.profileImageObjectKey == null &&
                            _state.value.isUserSelectedProfileImage
                        ) {
                            _sideEffect.send(SignUpSideEffect.ShowToast(R.string.sign_up_profile_image_upload_failed))
                            return@launch
                        }

                        _state.value.selectedUserType?.let { selectedUserType ->
                            when (selectedUserType) {
                                UserType.User -> {
                                    val currentState = _state.value
                                    val socialCode = tokenManager.getSocialCode()
                                    if (socialCode.isNullOrBlank()) {
                                        _state.update {
                                            it.copy(
                                                isSubmitting = false,
                                                error = AppError.Auth.SocialInfoMissing,
                                            )
                                        }
                                        _sideEffect.send(
                                            SignUpSideEffect.ShowToast(R.string.sign_up_social_info_missing),
                                        )
                                        return@launch
                                    }

                                    _state.update { it.copy(isSubmitting = true, error = null) }

                                    val signup =
                                        CustomerSignup(
                                            nickname = currentState.nickname,
                                            socialEmail = tokenManager.getSocialEmail(),
                                            socialProvider = tokenManager.getSocialProvider(),
                                            socialCode = socialCode,
                                            profileImage = currentState.profileImageObjectKey,
                                        )

                                    customerService.createCustomer(signup)
                                        .onSuccess {
                                            _state.update { it.copy(isSubmitting = false) }
                                            sendNavigateToSelectedSideEffect(
                                                destination = "sign-up-completion",
                                            )
                                        }
                                        .onFailure { error ->
                                            val appError = AppError.fromThrowable(error)
                                            _state.update {
                                                it.copy(
                                                    isSubmitting = false,
                                                    error = appError,
                                                )
                                            }
                                            _sideEffect.send(SignUpSideEffect.ShowToast(R.string.sign_up_submit_failed))
                                        }
                                }

                                UserType.Photographer -> {
                                    sendNavigateToSelectedSideEffect(
                                        destination = "sign-up-photographer",
                                    )
                                }
                            }
                        } ?: _sideEffect.send(SignUpSideEffect.ShowToast(R.string.sign_up_user_type_missing))
                    }
                }

                is SignUpCommonIntent.Navigate -> {
                    viewModelScope.launch {
                        _sideEffect.send(SignUpSideEffect.Navigate(intent.destination))
                    }
                }

                is SignUpCommonIntent.CompleteSignup -> {
                    viewModelScope.launch {
                        if (_state.value.isSubmitting) {
                            return@launch
                        }

                        _state.update { it.copy(isSubmitting = true, error = null) }

                        loginWithKakaoUseCase(intent.context)
                            .onSuccess { response ->
                                _state.update { it.copy(isSubmitting = false) }
                                if (response.registered && response.accessToken != null) {
                                    _sideEffect.send(SignUpSideEffect.SignupCompleted)
                                } else {
                                    _state.update {
                                        it.copy(error = AppError.Auth.SocialInfoMissing)
                                    }
                                    _sideEffect.send(SignUpSideEffect.ShowToast(R.string.sign_up_social_info_missing))
                                }
                            }
                            .onFailure { error ->
                                val appError = AppError.fromThrowable(error)
                                _state.update {
                                    it.copy(
                                        isSubmitting = false,
                                        error = appError,
                                    )
                                }
                                _sideEffect.send(SignUpSideEffect.ShowToast(R.string.sign_up_submit_failed))
                            }
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
                                val appError = AppError.fromThrowable(error)
                                _state.update {
                                    it.copy(
                                        isCheckingNickname = false,
                                        error = appError,
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

                SignUpCommonIntent.ProfileImageReadFailed -> {
                    _state.update {
                        it.copy(
                            profileImageUri = null,
                            profileImageObjectKey = null,
                            isUserSelectedProfileImage = false,
                            isUploadingImage = false,
                        )
                    }
                    viewModelScope.launch {
                        _sideEffect.send(
                            SignUpSideEffect.ShowToast(R.string.sign_up_profile_image_read_failed),
                        )
                    }
                }

                is SignUpCommonIntent.UploadProfileImage -> {
                    viewModelScope.launch {
                        Log.d(
                            TAG,
                            "profile image upload start: filename=${intent.filename}, bytes=${intent.imageBytes.size}",
                        )
                        _state.update { it.copy(isUploadingImage = true, error = null) }
                        s3Service.getUploadUrl("PROFILE", intent.filename)
                            .onSuccess { response ->
                                Log.d(TAG, "profile image upload url issued: objectKey=${response.objectKey}")
                                s3Service.uploadImage(response.uploadUrl, intent.imageBytes, intent.contentType)
                                    .onSuccess {
                                        Log.d(TAG, "profile image upload success: objectKey=${response.objectKey}")
                                        _state.update {
                                            it.copy(
                                                profileImageObjectKey = response.objectKey,
                                                isUploadingImage = false,
                                            )
                                        }
                                    }
                                    .onFailure { error ->
                                        val appError = AppError.fromThrowable(error)
                                        Log.e(TAG, "profile image S3 PUT failed", error)
                                        _state.update {
                                            it.copy(
                                                isUploadingImage = false,
                                                error = appError,
                                            )
                                        }
                                        _sideEffect.send(
                                            SignUpSideEffect.ShowToast(R.string.sign_up_profile_image_s3_failed),
                                        )
                                    }
                            }
                            .onFailure { error ->
                                val appError = AppError.fromThrowable(error)
                                Log.e(TAG, "profile image presigned URL failed", error)
                                _state.update {
                                    it.copy(
                                        isUploadingImage = false,
                                        error = appError,
                                    )
                                }
                                _sideEffect.send(
                                    SignUpSideEffect.ShowToast(R.string.sign_up_profile_image_url_failed),
                                )
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

        companion object {
            private const val TAG = "SignUpCommonViewModel"
        }
    }
