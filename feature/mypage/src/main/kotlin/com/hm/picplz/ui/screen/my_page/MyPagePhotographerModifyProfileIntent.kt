package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePhotographerModifyProfileIntent {
    data class ChangeNickname(val value: String) : MyPagePhotographerModifyProfileIntent

    data class ChangeInstagramId(val value: String) : MyPagePhotographerModifyProfileIntent

    data class ChangeIntroduction(val value: String) : MyPagePhotographerModifyProfileIntent

    data class ChangeProfileImage(val uri: String) : MyPagePhotographerModifyProfileIntent

    data object Save : MyPagePhotographerModifyProfileIntent

    data object NavigateBack : MyPagePhotographerModifyProfileIntent
}
