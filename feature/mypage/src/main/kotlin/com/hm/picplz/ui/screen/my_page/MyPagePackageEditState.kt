package com.hm.picplz.ui.screen.my_page

data class MyPagePackageEditState(
    val photographerId: Long = 0L,
    val packages: List<MyPagePackageItem> = emptyList(),
    val editMode: MyPagePackageEditMode = MyPagePackageEditMode.List,
    val draft: MyPagePackageDraft = MyPagePackageDraft(),
    val originalDraft: MyPagePackageDraft = MyPagePackageDraft(),
    val editingPackageId: Long? = null,
    val pendingDeletePackageId: Long? = null,
    val showUnsavedBackDialog: Boolean = false,
    val isSaving: Boolean = false,
    val isUploadingImage: Boolean = false,
    val isSaveEnabled: Boolean = false,
) {
    val hasUnsavedChanges: Boolean
        get() = editMode != MyPagePackageEditMode.List && draft != originalDraft

    companion object {
        fun idle() = MyPagePackageEditState()
    }
}

data class MyPagePackageItem(
    val id: Long,
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val price: Int,
    val imageUri: String,
    val imageObjectKey: String?,
)

data class MyPagePackageDraft(
    val name: String = "",
    val description: String = "",
    val durationMinutes: Int? = null,
    val price: Int = 0,
    val imageUri: String = "",
    val imageObjectKey: String? = null,
) {
    val hasRequiredFields: Boolean
        get() = name.isNotBlank() && durationMinutes != null
}

enum class MyPagePackageEditMode {
    List,
    Add,
    Edit,
}

enum class MyPagePackageDurationOption(
    val minutes: Int,
    val price: Int,
) {
    WITHIN_15(minutes = 15, price = 12900),
    MINUTES_15_TO_30(minutes = 30, price = 18900),
    MINUTES_30_TO_60(minutes = 60, price = 22900),
    OVER_60(minutes = 90, price = 29900),
}
