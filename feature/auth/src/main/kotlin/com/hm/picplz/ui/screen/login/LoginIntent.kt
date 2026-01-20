package com.hm.picplz.ui.screen.login

import android.content.Context

sealed interface LoginIntent {
    data class StartKakaoLogin(val context: Context) : LoginIntent

    data object FetchUserInfoFromKaKao : LoginIntent

    data object UnlinkKakao : LoginIntent
}
