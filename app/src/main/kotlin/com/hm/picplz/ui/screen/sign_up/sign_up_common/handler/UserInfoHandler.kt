package com.hm.picplz.ui.screen.sign_up.sign_up_common.handler

import com.hm.picplz.data.model.NicknameFieldError
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.SetNickname
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.SetProfileImageUri
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonState
import com.hm.picplz.ui.screen.sign_up.sign_up_common.validator.NicknameValidator

class UserInfoHandler {
    fun process(intent: SignUpCommonIntent, state: SignUpCommonState): SignUpCommonState? {
        return when (intent) {
            is SetNickname -> {
                val errors = NicknameValidator.validate(intent.newNickname)
                state.copy(
                    nickname = intent.newNickname,
                    nicknameFieldErrors = errors
                )
            }

            is SetProfileImageUri -> {
                state.copy(profileImageUri = intent.newProfileImageUri)
            }

            else -> null
        }
    }
}
