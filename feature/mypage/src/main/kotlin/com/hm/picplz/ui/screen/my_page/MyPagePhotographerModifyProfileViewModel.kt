package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.util.NicknameValidator
import com.hm.picplz.domain.usecase.CheckNicknameAvailabilityUseCase
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
    ) : ViewModel() {
        private val _state = MutableStateFlow(MyPagePhotographerModifyProfileState.idle())
        val state: StateFlow<MyPagePhotographerModifyProfileState> get() = _state

        private val _sideEffect = Channel<MyPagePhotographerModifyProfileSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private var duplicateCheckJob: Job? = null

        fun handleIntent(intent: MyPagePhotographerModifyProfileIntent) {
            when (intent) {
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
                is MyPagePhotographerModifyProfileIntent.Save -> save()
                is MyPagePhotographerModifyProfileIntent.NavigateBack -> navigateBack()
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
            if (!_state.value.isCompleteEnabled) {
                return
            }

            _state.update { it.copy(isSaving = true, saveErrorMessageResId = null) }
            _state.update {
                it.copy(
                    isSaving = false,
                    saveErrorMessageResId = R.string.modify_profile_save_unsupported,
                )
            }
        }

        private fun navigateBack() {
            viewModelScope.launch {
                _sideEffect.send(MyPagePhotographerModifyProfileSideEffect.NavigateBack)
            }
        }
    }
