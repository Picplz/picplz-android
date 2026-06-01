package com.hm.picplz.ui.screen.login

import com.hm.picplz.common.error.AppError

sealed interface LoginSideEffect {
    data object LoginSuccess : LoginSideEffect

    data class LoginFailed(val error: AppError?) : LoginSideEffect

    data class NavigateToSignUp(val profileImageUrl: String?) : LoginSideEffect

    data object UnlinkSuccess : LoginSideEffect

    data class UnlinkFailed(val error: AppError?) : LoginSideEffect
}
