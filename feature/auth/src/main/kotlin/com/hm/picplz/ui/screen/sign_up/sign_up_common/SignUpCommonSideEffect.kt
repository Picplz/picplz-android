package com.hm.picplz.ui.screen.sign_up.sign_up_common

import androidx.annotation.StringRes
import com.hm.picplz.common.model.User
import com.hm.picplz.navigation.model.NavigationRoute

sealed interface SignUpSideEffect {
    sealed interface SelectUserTypeScreenSideEffect : SignUpSideEffect {
        data class NavigateToSelected(
            val destination: String,
            val user: User,
        ) : SelectUserTypeScreenSideEffect
    }

    data object NavigateToPrev : SignUpSideEffect

    data class Navigate(val destination: NavigationRoute) : SignUpSideEffect

    data object ShowFileUploadDialog : SignUpSideEffect

    data class ShowToast(
        @StringRes val messageResId: Int,
    ) : SignUpSideEffect
}
