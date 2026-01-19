package com.hm.picplz.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.usecase.GetKakaoUserInfoUseCase
import com.hm.picplz.domain.usecase.LoginWithKakaoUseCase
import com.hm.picplz.domain.usecase.UnlinkKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    private val getKakaoUserInfoUseCase: GetKakaoUserInfoUseCase,
    private val unlinkKakaoUseCase: UnlinkKakaoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState.idle())
    val state: StateFlow<LoginState> get() = _state

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect: SharedFlow<LoginSideEffect> get() = _sideEffect

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.StartKakaoLogin -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    
                    loginWithKakaoUseCase(intent.context)
                        .onSuccess { response ->
                            _state.value = _state.value.copy(isLoading = false)
                            Log.d(TAG, "로그인 성공: $response")

                            if (response.registered) {
                                _sideEffect.emit(LoginSideEffect.LoginSuccess)
                            } else {
                                handleIntent(LoginIntent.FetchUserInfoFromKaKao)
                            }
                        }
                        .onFailure { error ->
                            _state.value = _state.value.copy(isLoading = false, error = error)
                            Log.e(TAG, "로그인 실패", error)
                            _sideEffect.emit(LoginSideEffect.LoginFailed(error))
                        }
                }
            }

            is LoginIntent.FetchUserInfoFromKaKao -> {
                viewModelScope.launch {
                    getKakaoUserInfoUseCase()
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

            is LoginIntent.UnlinkKakao -> {
                viewModelScope.launch {
                    unlinkKakaoUseCase()
                        .onSuccess {
                            Log.i(TAG, "연결 끊기 성공")
                            _sideEffect.emit(LoginSideEffect.UnlinkSuccess)
                        }
                        .onFailure { error ->
                            Log.e(TAG, "연결 끊기 실패", error)
                            _sideEffect.emit(LoginSideEffect.UnlinkFailed(error))
                        }
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
