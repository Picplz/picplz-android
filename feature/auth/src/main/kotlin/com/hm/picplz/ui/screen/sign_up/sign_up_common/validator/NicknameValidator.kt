package com.hm.picplz.ui.screen.sign_up.sign_up_common.validator

import com.hm.picplz.common.model.NicknameFieldError

object NicknameValidator {
    fun validate(newNickname: String): List<NicknameFieldError> {
        val errors = mutableListOf<NicknameFieldError>()
        if (newNickname.isEmpty()) {
            errors.add(NicknameFieldError.Required())
            return errors
        }
        // 우선순위: 형식 > 길이 > 중복
        if (!newNickname.matches(Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]+$"))) {
            errors.add(NicknameFieldError.InvalidChar())
            return errors
        }
        if (newNickname.length !in 2..15) {
            errors.add(NicknameFieldError.Length())
            return errors
        }
        // 중복 검사는 서버에서 처리
        return errors
    }
}
