package com.hm.picplz.ui.screen.login

sealed class LoginIntent {
    object NavigateToKaKao : LoginIntent()
    data class LoginSuccess(val token: String) : LoginIntent()
    data class LoginFailed(val error: Throwable?) : LoginIntent()
    data class LoginWithKaKao(val accessToken: String) : LoginIntent()
    object FetchUserInfoFromKaKao : LoginIntent()
}