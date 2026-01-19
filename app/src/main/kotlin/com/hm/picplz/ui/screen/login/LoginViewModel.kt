package com.hm.picplz.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    val TAG = "LOGININTROSCREEN"
    private val _state = MutableStateFlow(LoginState.idle())
    val state: StateFlow<LoginState> get() = _state

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect: SharedFlow<LoginSideEffect> get() = _sideEffect

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.NavigateToKaKao -> {
                viewModelScope.launch {
                    _sideEffect.emit(LoginSideEffect.NavigateToKaKao)
                }
            }

            is LoginIntent.LoginSuccess -> {
                viewModelScope.launch {
                    _sideEffect.emit(LoginSideEffect.LoginSuccess)
                }
            }

            is LoginIntent.LoginFailed -> {
                viewModelScope.launch {
                    _sideEffect.emit(LoginSideEffect.LoginFailed(intent.error))
                }
            }

            is LoginIntent.LoginWithKaKao -> {
                viewModelScope.launch {
                    val result = authService.loginWithKaKao(intent.accessToken)
                    result.onSuccess { res ->
                        Log.d(TAG, "성공!!! ${res} ")

                        // 이미 등록된 사용자라면 -> 고객으로 홈 화면 보내기
                        if (res.registered) {
                            _sideEffect.emit(LoginSideEffect.LoginSuccess)
                            // TODO: 전달 받은 AccessToken 저장
                        }
                        // 등록되지 않은 사용자라면 동의 정보 가져오기 -> 회원가입 화면으로 보내기
                        else {
                            handleIntent(LoginIntent.FetchUserInfoFromKaKao)
                        }
                    }.onFailure {
                        Log.e(TAG, it.message ?: "Unknown error")
                    }
                }
            }

            is LoginIntent.FetchUserInfoFromKaKao -> {
                viewModelScope.launch {
                    authService.getKakaoUserInfo()
                        .onSuccess { userInfo ->
                            Log.i(TAG, "사용자 정보 요청 성공\n프로필사진: ${userInfo.profileImageUrl}")
                            _sideEffect.emit(
                                LoginSideEffect.NavigateToSignUp(userInfo.profileImageUrl)
                            )
                        }
                        .onFailure { error ->
                            Log.e(TAG, "카카오 사용자 정보 요청 실패", error)
                            _sideEffect.emit(LoginSideEffect.LoginFailed(error))
                        }
                }
            }
        }
    }
}