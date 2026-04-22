package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePhotographerModifyProfileIntent {
    data object ClickPhotoPermissionAction : MyPagePhotographerModifyProfileIntent

    data object ClickProfileImage : MyPagePhotographerModifyProfileIntent

    data class SyncPhotoPermissionState(
        val granted: Boolean,
        val hasRequested: Boolean,
        val permanentlyDenied: Boolean,
    ) : MyPagePhotographerModifyProfileIntent

    data class ChangeNickname(val value: String) : MyPagePhotographerModifyProfileIntent

    data class ChangeInstagramId(val value: String) : MyPagePhotographerModifyProfileIntent

    data class ChangeIntroduction(val value: String) : MyPagePhotographerModifyProfileIntent

    data class ChangeProfileImage(val uri: String) : MyPagePhotographerModifyProfileIntent

    data class UploadProfileImage(
        val imageBytes: ByteArray,
        val filename: String,
    ) : MyPagePhotographerModifyProfileIntent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UploadProfileImage) return false
            return filename == other.filename && imageBytes.contentEquals(other.imageBytes)
        }

        override fun hashCode(): Int = 31 * imageBytes.contentHashCode() + filename.hashCode()
    }

    data object Save : MyPagePhotographerModifyProfileIntent

    data object NavigateBack : MyPagePhotographerModifyProfileIntent
}
