package com.hm.picplz.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.error.AppError
import com.hm.picplz.domain.usecase.GetKakaoUserInfoUseCase
import com.hm.picplz.domain.usecase.LoginWithKakaoUseCase
import com.hm.picplz.domain.usecase.UnlinkKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
        private val getKakaoUserInfoUseCase: GetKakaoUserInfoUseCase,
        private val unlinkKakaoUseCase: UnlinkKakaoUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(LoginState.idle())
        val state: StateFlow<LoginState> get() = _state

        private val _sideEffect = Channel<LoginSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: LoginIntent) {
            when (intent) {
                is LoginIntent.StartKakaoLogin -> {
                    viewModelScope.launch {
                        _state.update { it.copy(isLoading = true) }

                        loginWithKakaoUseCase(intent.context)
                            .onSuccess { response ->
                                _state.update { it.copy(isLoading = false) }
                                Log.d(TAG, "로그인 성공: registered=${response.registered}")

                                if (response.registered) {
                                    _sideEffect.send(LoginSideEffect.LoginSuccess)
                                } else {
                                    handleIntent(LoginIntent.FetchUserInfoFromKaKao)
                                }
                            }
                            .onFailure { error ->
                                val appError = AppError.fromThrowable(error)
                                _state.update { it.copy(isLoading = false, error = appError) }
                                Log.e(TAG, "로그인 실패", error)
                                _sideEffect.send(LoginSideEffect.LoginFailed(appError))
                            }
                    }
                }

                is LoginIntent.FetchUserInfoFromKaKao -> {
                    viewModelScope.launch {
                        getKakaoUserInfoUseCase()
                            .onSuccess { userInfo ->
                                Log.i(TAG, "사용자 정보 요청 성공\n프로필사진: ${userInfo.profileImageUrl}")
                                _sideEffect.send(
                                    LoginSideEffect.NavigateToSignUp(userInfo.profileImageUrl),
                                )
                            }
                            .onFailure { error ->
                                val appError = AppError.fromThrowable(error)
                                _state.update { it.copy(isLoading = false, error = appError) }
                                Log.e(TAG, "카카오 사용자 정보 요청 실패", error)
                                _sideEffect.send(LoginSideEffect.LoginFailed(appError))
                            }
                    }
                }

                is LoginIntent.UnlinkKakao -> {
                    viewModelScope.launch {
                        unlinkKakaoUseCase()
                            .onSuccess {
                                Log.i(TAG, "연결 끊기 성공")
                                _sideEffect.send(LoginSideEffect.UnlinkSuccess)
                            }
                            .onFailure { error ->
                                val appError = AppError.fromThrowable(error)
                                Log.e(TAG, "연결 끊기 실패", error)
                                _sideEffect.send(LoginSideEffect.UnlinkFailed(appError))
                            }
                    }
                }
            }
        }

        companion object {
            private const val TAG = "LoginViewModel"
        }
    }
