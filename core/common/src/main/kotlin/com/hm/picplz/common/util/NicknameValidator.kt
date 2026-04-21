package com.hm.picplz.common.util

import com.hm.picplz.common.model.NicknameFieldError

object NicknameValidator {
    fun validate(newNickname: String): List<NicknameFieldError> =
        when {
            newNickname.isEmpty() -> listOf(NicknameFieldError.Required())
            newNickname.any { it.isWhitespace() } -> listOf(NicknameFieldError.Whitespace())
            !newNickname.matches(Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]+$")) -> listOf(NicknameFieldError.InvalidChar())
            newNickname.length !in 2..15 -> listOf(NicknameFieldError.Length())
            else -> emptyList()
        }
}
