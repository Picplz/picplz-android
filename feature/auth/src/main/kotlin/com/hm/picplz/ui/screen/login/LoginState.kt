package com.hm.picplz.ui.screen.login

import com.hm.picplz.common.error.AppError

data class LoginState(
    val isLoading: Boolean = false,
    val error: AppError? = null,
) {
    companion object {
        fun idle(): LoginState {
            return LoginState()
        }
    }
}
