package com.hm.picplz.ui.screen.login

sealed interface LoginSideEffect {
    data object LoginSuccess : LoginSideEffect

    data class LoginFailed(val error: Throwable?) : LoginSideEffect

    data class NavigateToSignUp(val profileImageUrl: String?) : LoginSideEffect

    data object UnlinkSuccess : LoginSideEffect

    data class UnlinkFailed(val error: Throwable?) : LoginSideEffect
}
