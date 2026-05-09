package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.util.NicknameValidator
import com.hm.picplz.domain.model.UpdateMemberProfileCommand
import com.hm.picplz.domain.usecase.CheckNicknameAvailabilityUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetMemberProfileUseCase
import com.hm.picplz.domain.usecase.UpdateMemberProfileUseCase
import com.hm.picplz.domain.usecase.UploadProfileImageUseCase
import com.hm.picplz.feature.mypage.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPagePhotographerModifyProfileViewModel
    @Inject
    constructor(
        private val checkNicknameAvailabilityUseCase: CheckNicknameAvailabilityUseCase,
        private val getCurrentMemberIdUseCase: GetCurrentMemberIdUseCase,
        private val getMemberProfileUseCase: GetMemberProfileUseCase,
        private val updateMemberProfileUseCase: UpdateMemberProfileUseCase,
        private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MyPagePhotographerModifyProfileState.idle())
        val state: StateFlow<MyPagePhotographerModifyProfileState> get() = _state

        private val _sideEffect = Channel<MyPagePhotographerModifyProfileSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private var duplicateCheckJob: Job? = null

        init {
            loadMemberProfile()
        }

        fun handleIntent(intent: MyPagePhotographerModifyProfileIntent) {
            when (intent) {
                MyPagePhotographerModifyProfileIntent.ClickPhotoPermissionAction -> handlePhotoPermissionAction()
                MyPagePhotographerModifyProfileIntent.ClickProfileImage -> handleProfileImageClick()
                is MyPagePhotographerModifyProfileIntent.SyncPhotoPermissionState -> {
                    _state.update {
                        it.copy(
                            photoPermissionGranted = intent.granted,
                            hasRequestedPhotoPermission = intent.hasRequested,
                            isPhotoPermissionPermanentlyDenied = intent.permanentlyDenied,
                        )
                    }
                }
                is MyPagePhotographerModifyProfileIntent.ChangeNickname -> handleNicknameChange(intent.value)
                is MyPagePhotographerModifyProfileIntent.ChangeInstagramId -> {
                    _state.update { it.copy(instagramId = intent.value, saveErrorMessageResId = null) }
                }
                is MyPagePhotographerModifyProfileIntent.ChangeIntroduction -> {
                    _state.update { it.copy(introduction = intent.value, saveErrorMessageResId = null) }
                }
                is MyPagePhotographerModifyProfileIntent.ChangeProfileImage -> {
                    _state.update { it.copy(profileImageUri = intent.uri, saveErrorMessageResId = null) }
                }
                is MyPagePhotographerModifyProfileIntent.UploadProfileImage -> {
                    uploadProfileImage(intent.imageBytes, intent.filename)
                }
                is MyPagePhotographerModifyProfileIntent.Save -> save()
                is MyPagePhotographerModifyProfileIntent.NavigateBack -> navigateBack()
            }
        }

        private fun handleProfileImageClick() {
            viewModelScope.launch {
                if (_state.value.photoPermissionGranted) {
                    _sideEffect.send(MyPagePhotographerModifyProfileSideEffect.LaunchImagePicker)
                } else {
                    handlePhotoPermissionAction()
                }
            }
        }

        private fun handlePhotoPermissionAction() {
            viewModelScope.launch {
                if (_state.value.shouldOpenPhotoPermissionSettings) {
                    _sideEffect.send(MyPagePhotographerModifyProfileSideEffect.OpenPhotoPermissionSettings)
                } else {
                    _sideEffect.send(MyPagePhotographerModifyProfileSideEffect.RequestPhotoPermission)
                }
            }
        }

        private fun loadMemberProfile() {
            val memberId = getCurrentMemberIdUseCase()
            if (memberId == null) {
                viewModelScope.launch {
                    _sideEffect.send(
                        MyPagePhotographerModifyProfileSideEffect.ShowToast(
                            R.string.modify_profile_member_not_found,
                        ),
                    )
                }
                return
            }

            _state.update { it.copy(isLoading = true, memberId = memberId) }
            viewModelScope.launch {
                getMemberProfileUseCase(memberId)
                    .onSuccess { profile ->
                        _state.update {
                            it.copy(
                                memberId = profile.id,
                                originalNickname = profile.nickname,
                                nickname = profile.nickname,
                                originalInstagramId = profile.instagram.orEmpty(),
                                instagramId = profile.instagram.orEmpty(),
                                originalIntroduction = profile.introduction.orEmpty(),
                                introduction = profile.introduction.orEmpty(),
                                originalProfileImageObjectKey = profile.profileImage,
                                profileImageObjectKey = profile.profileImage,
                                originalProfileImageUri = profile.profileImage.orEmpty(),
                                uploadedProfileImageUri = profile.profileImage.orEmpty(),
                                profileImageUri = profile.profileImage.orEmpty(),
                                isLoading = false,
                                saveErrorMessageResId = null,
                            )
                        }
                    }.onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                saveErrorMessageResId = null,
                            )
                        }
                        _sideEffect.send(
                            MyPagePhotographerModifyProfileSideEffect.ShowToast(
                                R.string.modify_profile_load_failed,
                            ),
                        )
                    }
            }
        }

        private fun handleNicknameChange(newNickname: String) {
            duplicateCheckJob?.cancel()

            val localErrors = NicknameValidator.validate(newNickname)
            _state.update {
                it.copy(
                    nickname = newNickname,
                    nicknameFieldErrors = localErrors,
                    isCheckingNickname = false,
                    saveErrorMessageResId = null,
                )
            }

            val currentState = _state.value
            if (newNickname == currentState.originalNickname || localErrors.isNotEmpty()) {
                return
            }

            duplicateCheckJob =
                viewModelScope.launch {
                    _state.update { it.copy(isCheckingNickname = true) }

                    checkNicknameAvailabilityUseCase(newNickname)
                        .onSuccess { isAvailable ->
                            _state.update { state ->
                                if (state.nickname != newNickname) {
                                    state
                                } else {
                                    state.copy(
                                        isCheckingNickname = false,
                                        nicknameFieldErrors =
                                            if (isAvailable) {
                                                emptyList()
                                            } else {
                                                listOf(NicknameFieldError.DuplicateNickname())
                                            },
                                    )
                                }
                            }
                        }
                        .onFailure {
                            _state.update { state ->
                                if (state.nickname != newNickname) {
                                    state
                                } else {
                                    state.copy(isCheckingNickname = false)
                                }
                            }
                        }
                }
        }

        private fun save() {
            val currentState = _state.value
            if (!currentState.isCompleteEnabled || currentState.isSaving) {
                return
            }

            _state.update { it.copy(isSaving = true, saveErrorMessageResId = null) }
            viewModelScope.launch {
                updateMemberProfileUseCase(
                    UpdateMemberProfileCommand(
                        id = currentState.memberId ?: return@launch,
                        nickname = currentState.nickname,
                        profileImage = currentState.profileImageObjectKey,
                        introduction = currentState.introduction.ifBlank { null },
                        instagram = currentState.instagramId.ifBlank { null },
                    ),
                ).onSuccess {
                    _state.update {
                        it.copy(
                            originalNickname = it.nickname,
                            originalInstagramId = it.instagramId,
                            originalIntroduction = it.introduction,
                            originalProfileImageObjectKey = it.profileImageObjectKey,
                            originalProfileImageUri = it.profileImageUri,
                            uploadedProfileImageUri = it.profileImageUri,
                            isSaving = false,
                            saveErrorMessageResId = null,
                        )
                    }
                    _sideEffect.send(MyPagePhotographerModifyProfileSideEffect.NavigateBack)
                }.onFailure {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            saveErrorMessageResId = R.string.modify_profile_save_failed,
                        )
                    }
                }
            }
        }

        private fun uploadProfileImage(
            imageBytes: ByteArray,
            filename: String,
        ) {
            _state.update { it.copy(isUploadingImage = true, saveErrorMessageResId = null) }
            viewModelScope.launch {
                uploadProfileImageUseCase(imageBytes, filename)
                    .onSuccess { objectKey ->
                        _state.update {
                            it.copy(
                                profileImageObjectKey = objectKey,
                                uploadedProfileImageUri = it.profileImageUri,
                                isUploadingImage = false,
                            )
                        }
                    }.onFailure {
                        _state.update {
                            it.copy(
                                profileImageUri = it.uploadedProfileImageUri,
                                isUploadingImage = false,
                                saveErrorMessageResId = R.string.modify_profile_image_upload_failed,
                            )
                        }
                    }
            }
        }

        private fun navigateBack() {
            viewModelScope.launch {
                _sideEffect.send(MyPagePhotographerModifyProfileSideEffect.NavigateBack)
            }
        }
    }
