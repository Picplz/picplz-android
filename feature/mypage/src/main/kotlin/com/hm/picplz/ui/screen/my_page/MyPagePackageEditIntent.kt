package com.hm.picplz.ui.screen.my_page

sealed interface MyPagePackageEditIntent {
    data class LoadPhotographer(val photographerId: Long) : MyPagePackageEditIntent

    data object ClickAddPackage : MyPagePackageEditIntent

    data class ClickEditPackage(val packageId: Long) : MyPagePackageEditIntent

    data class ChangePackageName(val value: String) : MyPagePackageEditIntent

    data class ChangeDescription(val value: String) : MyPagePackageEditIntent

    data class SelectDuration(val option: MyPagePackageDurationOption) : MyPagePackageEditIntent

    data class ChangePackageImage(val uri: String) : MyPagePackageEditIntent

    data object ClickPackageImage : MyPagePackageEditIntent

    data class UploadPackageImage(
        val imageBytes: ByteArray,
        val filename: String,
    ) : MyPagePackageEditIntent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UploadPackageImage) return false
            return filename == other.filename && imageBytes.contentEquals(other.imageBytes)
        }

        override fun hashCode(): Int = 31 * imageBytes.contentHashCode() + filename.hashCode()
    }

    data object SavePackage : MyPagePackageEditIntent

    data class RequestDeletePackage(val packageId: Long) : MyPagePackageEditIntent

    data object DismissDeleteDialog : MyPagePackageEditIntent

    data object ConfirmDeletePackage : MyPagePackageEditIntent

    data object NavigateBack : MyPagePackageEditIntent

    data object DismissUnsavedBackDialog : MyPagePackageEditIntent

    data object ConfirmDiscardChanges : MyPagePackageEditIntent
}
