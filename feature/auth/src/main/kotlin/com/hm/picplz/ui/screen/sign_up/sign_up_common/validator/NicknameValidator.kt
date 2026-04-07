package com.hm.picplz.ui.screen.sign_up.sign_up_common.validator

import com.hm.picplz.common.model.NicknameFieldError
import com.hm.picplz.common.util.NicknameValidator as CommonNicknameValidator

object NicknameValidator {
    fun validate(newNickname: String): List<NicknameFieldError> = CommonNicknameValidator.validate(newNickname)
}
