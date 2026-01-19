package com.hm.picplz.ui.screen.login

sealed interface LoginSideEffect {
    data object NavigateToKaKao : LoginSideEffect
    data object LoginSuccess : LoginSideEffect
    data class LoginFailed(val error: Throwable?) : LoginSideEffect
    data class NavigateToSignUp(val profileImageUrl: String?) : LoginSideEffect
}