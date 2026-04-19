package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.common.model.NicknameFieldError

data class MyPagePhotographerModifyProfileState(
    val originalNickname: String,
    val nickname: String,
    val originalInstagramId: String,
    val instagramId: String,
    val originalIntroduction: String,
    val introduction: String,
    val originalProfileImageUri: String,
    val profileImageUri: String,
    val nicknameFieldErrors: List<NicknameFieldError> = emptyList(),
    val isCheckingNickname: Boolean = false,
    val isSaving: Boolean = false,
    val saveErrorMessageResId: Int? = null,
) {
    val representativeNicknameError: NicknameFieldError?
        get() = nicknameFieldErrors.firstOrNull()

    val hasChanges: Boolean
        get() =
            nickname != originalNickname ||
                instagramId != originalInstagramId ||
                introduction != originalIntroduction ||
                profileImageUri != originalProfileImageUri

    val isFormValid: Boolean
        get() = nickname.isNotBlank() && nicknameFieldErrors.isEmpty() && !isCheckingNickname && !isSaving

    val isCompleteEnabled: Boolean
        get() = hasChanges && isFormValid

    companion object {
        fun idle() =
            MyPagePhotographerModifyProfileState(
                originalNickname = "유가영",
                nickname = "유가영",
                originalInstagramId = "imdooring",
                instagramId = "imdooring",
                originalIntroduction = "안녕하세요, 임두현 사진작가입니다.",
                introduction = "안녕하세요, 임두현 사진작가입니다.",
                originalProfileImageUri = "",
                profileImageUri = "",
            )
    }
}
