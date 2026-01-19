package com.hm.picplz.ui.screen.login

sealed interface LoginIntent {
    data object NavigateToKaKao : LoginIntent
    data class LoginSuccess(val token: String) : LoginIntent
    data class LoginFailed(val error: Throwable?) : LoginIntent
    data class LoginWithKaKao(val accessToken: String) : LoginIntent
    data object FetchUserInfoFromKaKao : LoginIntent
}