package com.hm.picplz.ui.screen.sign_up.sign_up_common.handler

import com.hm.picplz.common.model.SelectionState
import com.hm.picplz.common.model.UserType
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.ClickUserTypeButton
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.ResetSelectedUserType
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonState

class UserTypeInfoHandler {
    fun process(intent: SignUpCommonIntent, state: SignUpCommonState): SignUpCommonState? {
        return when (intent) {
            is ClickUserTypeButton -> {
                val newUserType = if (state.selectedUserType == intent.userType) {
                    null
                } else {
                    intent.userType
                }
                val newPhotographerSelectionState = if (newUserType == UserType.Photographer) {
                    SelectionState.SELECTED
                } else if (newUserType == UserType.User) {
                    SelectionState.DESELECTED
                } else {
                    SelectionState.UNSELECTED
                }
                val newUserSelectionState = if (newUserType == UserType.User) {
                    SelectionState.SELECTED
                } else if (newUserType == UserType.Photographer) {
                    SelectionState.DESELECTED
                } else {
                    SelectionState.UNSELECTED
                }

                state.copy(
                    selectedUserType = newUserType,
                    photographerSelectionState = newPhotographerSelectionState,
                    userSelectionState = newUserSelectionState
                )
            }

            is ResetSelectedUserType -> {
                state.copy(selectedUserType = null)
            }

            else -> null
        }
    }
}
