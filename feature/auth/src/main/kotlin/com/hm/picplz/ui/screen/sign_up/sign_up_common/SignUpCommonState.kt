package com.hm.picplz.ui.screen.sign_up.sign_up_common

import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.model.SelectionState
import com.hm.picplz.common.model.UserType

data class SignUpCommonState(
    val currentStep: Int = 0,
    val selectedUserType: UserType? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val nickname: String = "",
    val profileImageUri: String? = null,
    val profileImageObjectKey: String? = null,
    val isUploadingImage: Boolean = false,
    val isSubmitting: Boolean = false,
    val nicknameFieldErrors: List<NicknameFieldError> = emptyList(),
    val isCheckingNickname: Boolean = false,
    val isNicknameDuplicate: Boolean = false,
    val photographerSelectionState: SelectionState,
    val userSelectionState: SelectionState,
) {
    companion object {
        fun idle(): SignUpCommonState {
            return SignUpCommonState(
                currentStep = 0,
                selectedUserType = null,
                isLoading = false,
                error = null,
                nickname = "",
                profileImageUri = null,
                profileImageObjectKey = null,
                isUploadingImage = false,
                isSubmitting = false,
                nicknameFieldErrors = emptyList(),
                isCheckingNickname = false,
                isNicknameDuplicate = false,
                photographerSelectionState = SelectionState.UNSELECTED,
                userSelectionState = SelectionState.UNSELECTED,
            )
        }
    }
}
