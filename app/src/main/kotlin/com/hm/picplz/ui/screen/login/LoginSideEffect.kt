package com.hm.picplz.ui.screen.login

sealed class LoginSideEffect {
    object NavigateToKaKao : LoginSideEffect()
    object LoginSuccess : LoginSideEffect()
    data class LoginFailed(val error: Throwable?) : LoginSideEffect()
    data class NavigateToSignUp(val profileImageUrl: String?) : LoginSideEffect()
}