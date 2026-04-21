package com.hm.picplz.common.model

sealed class NicknameFieldError {
    abstract val message: String

    data class Required(override val message: String = "닉네임을 작성해주세요") : NicknameFieldError()

    data class InvalidChar(override val message: String = "한글,영문,숫자만 입력해 주세요. (2~15자)") : NicknameFieldError()

    data class Length(override val message: String = "닉네임은 2~15자로 입력해 주세요.") : NicknameFieldError()

    data class DuplicateNickname(override val message: String = "중복된 닉네임입니다.") : NicknameFieldError()

    data class Whitespace(override val message: String = "닉네임에 공백 사용은 불가능합니다.") : NicknameFieldError()

    data class Custom(override val message: String) : NicknameFieldError()
}
