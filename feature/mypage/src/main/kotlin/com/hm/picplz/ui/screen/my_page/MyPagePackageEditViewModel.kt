package com.hm.picplz.ui.screen.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.CreateProductCommand
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.domain.usecase.CreateProductUseCase
import com.hm.picplz.domain.usecase.GetPhotographerProductsUseCase
import com.hm.picplz.domain.usecase.UploadProductImageUseCase
import com.hm.picplz.feature.mypage.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPagePackageEditViewModel
    @Inject
    constructor(
        private val createProductUseCase: CreateProductUseCase,
        private val getPhotographerProductsUseCase: GetPhotographerProductsUseCase,
        private val uploadProductImageUseCase: UploadProductImageUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MyPagePackageEditState.idle())
        val state: StateFlow<MyPagePackageEditState> = _state

        private val _sideEffect = Channel<MyPagePackageEditSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: MyPagePackageEditIntent) {
            when (intent) {
                is MyPagePackageEditIntent.LoadPhotographer -> {
                    loadPhotographerProducts(intent.photographerId)
                }
                MyPagePackageEditIntent.ClickAddPackage -> openAddForm()
                is MyPagePackageEditIntent.ClickEditPackage -> {
                    sendSideEffect(MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_option_pending))
                }
                is MyPagePackageEditIntent.ChangePackageName -> {
                    updateDraft { it.copy(name = intent.value.take(MAX_PACKAGE_NAME_LENGTH)) }
                }
                is MyPagePackageEditIntent.ChangeDescription -> {
                    updateDraft { it.copy(description = intent.value.take(MAX_PACKAGE_DESCRIPTION_LENGTH)) }
                }
                is MyPagePackageEditIntent.SelectDuration -> {
                    updateDraft {
                        it.copy(
                            durationMinutes = intent.option.minutes,
                            price = intent.option.price,
                        )
                    }
                }
                is MyPagePackageEditIntent.ChangePackageImage -> updateDraft { it.copy(imageUri = intent.uri) }
                MyPagePackageEditIntent.ClickPackageImage -> {
                    sendSideEffect(MyPagePackageEditSideEffect.LaunchImagePicker)
                }
                is MyPagePackageEditIntent.UploadPackageImage -> uploadPackageImage(intent.imageBytes, intent.filename)
                MyPagePackageEditIntent.SavePackage -> savePackage()
                is MyPagePackageEditIntent.RequestDeletePackage -> {
                    sendSideEffect(MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_option_pending))
                }
                MyPagePackageEditIntent.DismissDeleteDialog -> {
                    _state.update { it.copy(pendingDeletePackageId = null) }
                }
                MyPagePackageEditIntent.ConfirmDeletePackage -> deletePackage()
                is MyPagePackageEditIntent.NavigateBack -> navigateBack()
                MyPagePackageEditIntent.DismissUnsavedBackDialog -> {
                    _state.update { it.copy(showUnsavedBackDialog = false) }
                }
                MyPagePackageEditIntent.ConfirmDiscardChanges -> closeForm()
            }
        }

        private fun loadPhotographerProducts(photographerId: Long) {
            _state.update { it.copy(photographerId = photographerId) }
            if (photographerId <= 0) return

            viewModelScope.launch {
                getPhotographerProductsUseCase(photographerId)
                    .onSuccess { products ->
                        _state.update { state ->
                            state.copy(packages = products.map { it.toPackageItem() })
                        }
                    }.onFailure {
                        _sideEffect.send(MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_load_failed))
                    }
            }
        }

        private fun openAddForm() {
            val currentState = _state.value
            if (currentState.packages.size >= MAX_PACKAGE_COUNT) {
                sendSideEffect(MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_max_count_error))
                return
            }

            _state.update {
                it.copy(
                    editMode = MyPagePackageEditMode.Add,
                    draft = MyPagePackageDraft(),
                    originalDraft = MyPagePackageDraft(),
                    editingPackageId = null,
                    isSaveEnabled = false,
                )
            }
        }

        private fun updateDraft(transform: (MyPagePackageDraft) -> MyPagePackageDraft) {
            _state.update {
                val draft = transform(it.draft)
                it.copy(
                    draft = draft,
                    isSaveEnabled = draft.hasRequiredFields && !it.isSaving && !it.isUploadingImage,
                )
            }
        }

        private fun uploadPackageImage(
            imageBytes: ByteArray,
            filename: String,
        ) {
            viewModelScope.launch {
                _state.update { it.copy(isUploadingImage = true, isSaveEnabled = false) }
                uploadProductImageUseCase(imageBytes, filename)
                    .onSuccess { objectKey ->
                        _state.update {
                            val draft = it.draft.copy(imageObjectKey = objectKey)
                            it.copy(
                                draft = draft,
                                isUploadingImage = false,
                                isSaveEnabled = draft.hasRequiredFields,
                            )
                        }
                    }.onFailure {
                        _state.update { state ->
                            val draft = state.draft.copy(imageUri = "", imageObjectKey = null)
                            state.copy(
                                draft = draft,
                                isUploadingImage = false,
                                isSaveEnabled = draft.hasRequiredFields,
                            )
                        }
                        _sideEffect.send(
                            MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_image_upload_failed),
                        )
                    }
            }
        }

        private fun savePackage() {
            val currentState = _state.value
            if (currentState.isSaving || currentState.isUploadingImage) return

            val draft = currentState.draft
            val durationMinutes = draft.durationMinutes ?: return
            if (!draft.hasRequiredFields) return

            when (currentState.editMode) {
                MyPagePackageEditMode.Add -> createPackage(currentState, draft, durationMinutes)
                MyPagePackageEditMode.Edit -> updatePackage(currentState, draft, durationMinutes)
                MyPagePackageEditMode.List -> Unit
            }
        }

        private fun createPackage(
            currentState: MyPagePackageEditState,
            draft: MyPagePackageDraft,
            durationMinutes: Int,
        ) {
            if (currentState.photographerId <= 0) {
                sendSideEffect(MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_photographer_id_missing))
                return
            }

            viewModelScope.launch {
                _state.update { it.copy(isSaving = true, isSaveEnabled = false) }
                createProductUseCase(
                    CreateProductCommand(
                        photographerId = currentState.photographerId,
                        name = draft.name.trim(),
                        description = draft.description,
                        shootPrice = draft.price,
                        shootDuration = durationMinutes,
                        otherDetails = draft.description,
                        productPhotos = listOfNotNull(draft.imageObjectKey),
                    ),
                ).onSuccess { productId ->
                    val newPackage =
                        MyPagePackageItem(
                            id = productId,
                            name = draft.name.trim(),
                            description = draft.description,
                            durationMinutes = durationMinutes,
                            price = draft.price,
                            imageUri = draft.imageUri,
                            imageObjectKey = draft.imageObjectKey,
                        )
                    _state.update {
                        it.copy(
                            packages = it.packages + newPackage,
                            editMode = MyPagePackageEditMode.List,
                            draft = MyPagePackageDraft(),
                            originalDraft = MyPagePackageDraft(),
                            editingPackageId = null,
                            isSaving = false,
                            isSaveEnabled = false,
                        )
                    }
                }.onFailure {
                    _state.update { it.copy(isSaving = false, isSaveEnabled = it.draft.hasRequiredFields) }
                    _sideEffect.send(MyPagePackageEditSideEffect.ShowToast(R.string.package_edit_save_failed))
                }
            }
        }

        private fun updatePackage(
            currentState: MyPagePackageEditState,
            draft: MyPagePackageDraft,
            durationMinutes: Int,
        ) {
            val packageId = currentState.editingPackageId ?: return
            _state.update {
                it.copy(
                    packages =
                        it.packages.map { item ->
                            if (item.id == packageId) {
                                item.copy(
                                    name = draft.name.trim(),
                                    description = draft.description,
                                    durationMinutes = durationMinutes,
                                    price = draft.price,
                                    imageUri = draft.imageUri,
                                    imageObjectKey = draft.imageObjectKey,
                                )
                            } else {
                                item
                            }
                        },
                    editMode = MyPagePackageEditMode.List,
                    draft = MyPagePackageDraft(),
                    originalDraft = MyPagePackageDraft(),
                    editingPackageId = null,
                    isSaveEnabled = false,
                )
            }
        }

        private fun deletePackage() {
            val packageId = _state.value.pendingDeletePackageId ?: return
            _state.update {
                it.copy(
                    packages = it.packages.filterNot { item -> item.id == packageId },
                    pendingDeletePackageId = null,
                    editMode = if (it.editingPackageId == packageId) MyPagePackageEditMode.List else it.editMode,
                    editingPackageId = if (it.editingPackageId == packageId) null else it.editingPackageId,
                    draft = if (it.editingPackageId == packageId) MyPagePackageDraft() else it.draft,
                    originalDraft = if (it.editingPackageId == packageId) MyPagePackageDraft() else it.originalDraft,
                    isSaveEnabled = if (it.editingPackageId == packageId) false else it.isSaveEnabled,
                )
            }
        }

        private fun navigateBack() {
            val currentState = _state.value
            when {
                currentState.hasUnsavedChanges -> _state.update { it.copy(showUnsavedBackDialog = true) }
                currentState.editMode != MyPagePackageEditMode.List -> closeForm()
                else -> sendSideEffect(MyPagePackageEditSideEffect.NavigateBack)
            }
        }

        private fun closeForm() {
            _state.update {
                it.copy(
                    editMode = MyPagePackageEditMode.List,
                    draft = MyPagePackageDraft(),
                    originalDraft = MyPagePackageDraft(),
                    editingPackageId = null,
                    showUnsavedBackDialog = false,
                    isSaveEnabled = false,
                )
            }
        }

        private fun sendSideEffect(effect: MyPagePackageEditSideEffect) {
            viewModelScope.launch {
                _sideEffect.send(effect)
            }
        }

        private fun ShootingPackage.toPackageItem(): MyPagePackageItem =
            MyPagePackageItem(
                id = packageId,
                name = title,
                description = description,
                durationMinutes = shootingTime.toDurationMinutes(),
                price = price,
                imageUri = imageUri,
                imageObjectKey = imageUri.ifBlank { null },
            )

        private fun String.toDurationMinutes(): Int =
            when {
                contains("15~30") -> 30
                contains("30분~1시간") -> 60
                contains("1시간") -> 90
                contains("15분") -> 15
                else -> filter { it.isDigit() }.toIntOrNull() ?: 0
            }

        private companion object {
            const val MAX_PACKAGE_COUNT = 3
            const val MAX_PACKAGE_NAME_LENGTH = 15
            const val MAX_PACKAGE_DESCRIPTION_LENGTH = 300
        }
    }
