package com.hm.picplz.ui.screen.sign_up.sign_up_client

import com.hm.picplz.common.model.User

sealed interface SignUpClientIntent {
    data class SetUserInfo(val userInfo: User) : SignUpClientIntent

    data object NavigateToPrev : SignUpClientIntent
}
