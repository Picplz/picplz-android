package com.hm.picplz.ui.screen.sign_up.sign_up_common

import com.hm.picplz.common.model.UserType
import com.hm.picplz.navigation.model.NavigationRoute

sealed interface SignUpCommonIntent {
    data object NavigateToPrev : SignUpCommonIntent

    data class ClickUserTypeButton(val userType: UserType) : SignUpCommonIntent

    data object NavigateToSelected : SignUpCommonIntent

    data object ResetSelectedUserType : SignUpCommonIntent

    data class SetNickname(val newNickname: String) : SignUpCommonIntent

    data object CheckNicknameDuplicate : SignUpCommonIntent

    data class SetProfileImageUri(val newProfileImageUri: String?) : SignUpCommonIntent

    data class Navigate(val destination: NavigationRoute) : SignUpCommonIntent

    data object ShowFileUploadDialog : SignUpCommonIntent

    data object ResetAllSignUpData : SignUpCommonIntent
}
