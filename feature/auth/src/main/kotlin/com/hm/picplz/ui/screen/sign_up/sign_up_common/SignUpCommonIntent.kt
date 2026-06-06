package com.hm.picplz.ui.screen.sign_up.sign_up_common

import android.content.Context
import com.hm.picplz.common.model.UserType
import com.hm.picplz.navigation.model.NavigationRoute

sealed interface SignUpCommonIntent {
    data object NavigateToPrev : SignUpCommonIntent

    data class ClickUserTypeButton(val userType: UserType) : SignUpCommonIntent

    data object NavigateToSelected : SignUpCommonIntent

    data object ResetSelectedUserType : SignUpCommonIntent

    data class SetNickname(val newNickname: String) : SignUpCommonIntent

    data object CheckNicknameDuplicate : SignUpCommonIntent

    data class SetProfileImageUri(
        val newProfileImageUri: String?,
        val isUserSelected: Boolean,
    ) : SignUpCommonIntent

    data class UploadProfileImage(
        val imageBytes: ByteArray,
        val filename: String,
        val contentType: String,
    ) : SignUpCommonIntent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UploadProfileImage) return false
            return filename == other.filename &&
                contentType == other.contentType &&
                imageBytes.contentEquals(other.imageBytes)
        }

        override fun hashCode(): Int {
            var result = imageBytes.contentHashCode()
            result = 31 * result + filename.hashCode()
            result = 31 * result + contentType.hashCode()
            return result
        }
    }

    data object ProfileImageReadFailed : SignUpCommonIntent

    data class Navigate(val destination: NavigationRoute) : SignUpCommonIntent

    data class CompleteSignup(val context: Context) : SignUpCommonIntent

    data object ShowFileUploadDialog : SignUpCommonIntent

    data object ResetAllSignUpData : SignUpCommonIntent
}
