package com.hm.picplz.common.util

import com.hm.picplz.common.model.NicknameFieldError

object NicknameValidator {
    fun validate(newNickname: String): List<NicknameFieldError> {
        val errors = mutableListOf<NicknameFieldError>()
        if (newNickname.isEmpty()) {
            errors.add(NicknameFieldError.Required())
            return errors
        }
        if (!newNickname.matches(Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]+$"))) {
            errors.add(NicknameFieldError.InvalidChar())
            return errors
        }
        if (newNickname.length !in 2..15) {
            errors.add(NicknameFieldError.Length())
            return errors
        }
        return errors
    }
}
