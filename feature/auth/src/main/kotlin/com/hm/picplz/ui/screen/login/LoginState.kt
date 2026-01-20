package com.hm.picplz.ui.screen.login

data class LoginState(
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    companion object {
        fun idle(): LoginState {
            return LoginState()
        }
    }
}