package com.hm.picplz.ui.screen.sign_up.sign_up_common

import com.hm.picplz.common.model.User

sealed interface SignUpSideEffect {
    sealed interface SelectUserTypeScreenSideEffect : SignUpSideEffect {
        data class NavigateToSelected(
            val destination: String,
            val user: User,
        ) : SelectUserTypeScreenSideEffect
    }

    data object NavigateToPrev : SignUpSideEffect

    data class Navigate(val destination: String) : SignUpSideEffect

    data object ShowFileUploadDialog : SignUpSideEffect
}
