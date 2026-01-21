package com.hm.picplz.ui.screen.sign_up.sign_up_photographer

import com.hm.picplz.common.model.User
import com.hm.picplz.navigation.model.NavigationRoute

sealed interface SignUpPhotographerSideEffect {
    data object NavigateToPrev : SignUpPhotographerSideEffect

    data class Navigate(val destination: NavigationRoute) : SignUpPhotographerSideEffect

    data class NavigateToSignUpCompletion(val userInfo: User) : SignUpPhotographerSideEffect
}
