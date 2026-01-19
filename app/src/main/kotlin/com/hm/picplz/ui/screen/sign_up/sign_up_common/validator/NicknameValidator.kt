package com.hm.picplz.ui.screen.sign_up.sign_up_common.validator

import com.hm.picplz.data.model.NicknameFieldError

object NicknameValidator {
    fun validate(newNickname: String): List<NicknameFieldError> {
        val errors = mutableListOf<NicknameFieldError>()
        if (newNickname.isEmpty()) {
            errors.add(NicknameFieldError.Required())
        }
        if (newNickname.length < 2 || newNickname.length > 15) {
            errors.add(NicknameFieldError.Length())
        }
        if (!newNickname.matches(Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s]+$"))) {
            errors.add(NicknameFieldError.InvalidChar())
        }
        if (newNickname.contains(Regex("[\\p{So}\\p{Cn}\\p{Sk}\\p{Sc}\\p{Sm}]"))) {
            errors.add(NicknameFieldError.InvalidSpecialCharacter())
        }
        if (newNickname.startsWith(" ") || newNickname.endsWith(" ")) {
            errors.add(NicknameFieldError.Whitespace())
        }
        return errors
    }
}
