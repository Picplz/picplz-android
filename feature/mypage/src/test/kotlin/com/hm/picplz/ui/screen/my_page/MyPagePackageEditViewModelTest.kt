package com.hm.picplz.ui.screen.my_page

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.CreateProductCommand
import com.hm.picplz.domain.model.ShootingPackage
import com.hm.picplz.domain.repository.ProductRepository
import com.hm.picplz.domain.repository.S3Repository
import com.hm.picplz.domain.usecase.CreateProductUseCase
import com.hm.picplz.domain.usecase.GetPhotographerProductsUseCase
import com.hm.picplz.domain.usecase.UploadProductImageUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPagePackageEditViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state disables save and exposes empty package list`() {
        val viewModel = createViewModel()

        assertFalse(viewModel.state.value.isSaveEnabled)
        assertEquals(emptyList<MyPagePackageItem>(), viewModel.state.value.packages)
        assertEquals(MyPagePackageEditMode.List, viewModel.state.value.editMode)
    }

    @Test
    fun `navigate back emits navigation side effect`() =
        runTest {
            val viewModel = createViewModel()
            val sideEffectDeferred = async { viewModel.sideEffect.first() }

            viewModel.handleIntent(MyPagePackageEditIntent.NavigateBack)
            advanceUntilIdle()

            assertEquals(
                MyPagePackageEditSideEffect.NavigateBack,
                sideEffectDeferred.await(),
            )
        }

    @Test
    fun `click add opens blank package form directly`() {
        val viewModel = createViewModel()

        viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)

        val state = viewModel.state.value
        assertEquals(MyPagePackageEditMode.Add, state.editMode)
        assertEquals("", state.draft.name)
        assertEquals(null, state.draft.durationMinutes)
        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `load photographer displays existing products`() =
        runTest {
            val productRepository =
                FakePackagePhotographerRepository(
                    products =
                        listOf(
                            ShootingPackage(
                                packageId = 7,
                                title = "Existing Package",
                                price = 90000,
                                imageUri = "products/existing.jpg",
                                shootingTime = "60분",
                                description = "Existing description",
                            ),
                        ),
                )
            val viewModel = createViewModel(photographerRepository = productRepository)
            advanceUntilIdle()

            val item = viewModel.state.value.packages.single()
            assertEquals(12L, productRepository.requestedPhotographerId)
            assertEquals(7L, item.id)
            assertEquals("Existing Package", item.name)
            assertEquals(60, item.durationMinutes)
            assertEquals(90000, item.price)
        }

    @Test
    fun `name and duration are required to enable save`() {
        val viewModel = createViewModel()

        viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
        viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageName("Profile Package"))
        assertFalse(viewModel.state.value.isSaveEnabled)

        viewModel.handleIntent(MyPagePackageEditIntent.SelectDuration(MyPagePackageDurationOption.MINUTES_15_TO_30))

        assertEquals(30, viewModel.state.value.draft.durationMinutes)
        assertEquals(18900, viewModel.state.value.draft.price)
        assertEquals(true, viewModel.state.value.isSaveEnabled)
    }

    @Test
    fun `limits package name and description length`() {
        val viewModel = createViewModel()

        viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
        viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageName("1234567890123456"))
        viewModel.handleIntent(MyPagePackageEditIntent.ChangeDescription("a".repeat(301)))

        assertEquals(15, viewModel.state.value.draft.name.length)
        assertEquals(300, viewModel.state.value.draft.description.length)
    }

    @Test
    fun `save add posts product and returns to list`() =
        runTest {
            val photographerRepository = FakePackagePhotographerRepository()
            val viewModel = createViewModel(photographerRepository = photographerRepository)

            viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
            viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageName("Profile Package"))
            viewModel.handleIntent(MyPagePackageEditIntent.ChangeDescription("Outdoor shoot"))
            viewModel.handleIntent(MyPagePackageEditIntent.SelectDuration(MyPagePackageDurationOption.MINUTES_30_TO_60))
            viewModel.handleIntent(MyPagePackageEditIntent.SavePackage)
            advanceUntilIdle()

            val request = photographerRepository.createdProducts.single()
            assertEquals(12L, request.photographerId)
            assertEquals("Profile Package", request.name)
            assertEquals("Outdoor shoot", request.description)
            assertEquals(60, request.shootDuration)
            assertEquals(22900, request.shootPrice)
            assertEquals(null, request.editedYn)
            assertEquals(101L, viewModel.state.value.packages.single().id)
            assertEquals(listOf<MyPagePackageItem>().size + 1, viewModel.state.value.packages.size)
            assertEquals(MyPagePackageEditMode.List, viewModel.state.value.editMode)
        }

    @Test
    fun `edit option emits pending toast without changing package`() =
        runTest {
            val photographerRepository = FakePackagePhotographerRepository()
            val viewModel = createViewModel(photographerRepository = photographerRepository)
            viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
            viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageName("Original"))
            viewModel.handleIntent(MyPagePackageEditIntent.SelectDuration(MyPagePackageDurationOption.MINUTES_15_TO_30))
            viewModel.handleIntent(MyPagePackageEditIntent.SavePackage)
            advanceUntilIdle()

            val packageId = viewModel.state.value.packages.single().id
            val sideEffectDeferred = async { viewModel.sideEffect.first() }
            viewModel.handleIntent(MyPagePackageEditIntent.ClickEditPackage(packageId))
            advanceUntilIdle()

            assertEquals(
                MyPagePackageEditSideEffect.ShowToast(
                    com.hm.picplz.feature.mypage.R.string.package_edit_option_pending,
                ),
                sideEffectDeferred.await(),
            )
            assertEquals(MyPagePackageEditMode.List, viewModel.state.value.editMode)
            assertEquals("Original", viewModel.state.value.packages.single().name)
        }

    @Test
    fun `delete option emits pending toast without deleting package`() =
        runTest {
            val photographerRepository = FakePackagePhotographerRepository()
            val viewModel = createViewModel(photographerRepository = photographerRepository)
            viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
            viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageName("Original"))
            viewModel.handleIntent(MyPagePackageEditIntent.SelectDuration(MyPagePackageDurationOption.MINUTES_15_TO_30))
            viewModel.handleIntent(MyPagePackageEditIntent.SavePackage)
            advanceUntilIdle()

            val packageId = viewModel.state.value.packages.single().id
            val sideEffectDeferred = async { viewModel.sideEffect.first() }
            viewModel.handleIntent(MyPagePackageEditIntent.RequestDeletePackage(packageId))
            advanceUntilIdle()

            assertEquals(
                MyPagePackageEditSideEffect.ShowToast(
                    com.hm.picplz.feature.mypage.R.string.package_edit_option_pending,
                ),
                sideEffectDeferred.await(),
            )
            assertEquals(1, viewModel.state.value.packages.size)
            assertEquals(null, viewModel.state.value.pendingDeletePackageId)
        }

    @Test
    fun `unsaved back confirms before returning to package list`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
            viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageName("Unsaved"))
            viewModel.handleIntent(MyPagePackageEditIntent.NavigateBack)
            advanceUntilIdle()

            assertEquals(true, viewModel.state.value.showUnsavedBackDialog)

            viewModel.handleIntent(MyPagePackageEditIntent.ConfirmDiscardChanges)
            advanceUntilIdle()

            assertEquals(false, viewModel.state.value.showUnsavedBackDialog)
            assertEquals(MyPagePackageEditMode.List, viewModel.state.value.editMode)
            assertEquals(MyPagePackageDraft(), viewModel.state.value.draft)
        }

    @Test
    fun `upload package image stores uploaded object key`() =
        runTest {
            val s3Repository = FakePackageS3Repository(uploadedObjectKey = "products/image.jpg")
            val viewModel = createViewModel(s3Repository = s3Repository)

            viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
            viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageImage("content://image"))
            viewModel.handleIntent(MyPagePackageEditIntent.UploadPackageImage(byteArrayOf(1, 2), "image.jpg"))
            advanceUntilIdle()

            assertEquals("products/image.jpg", viewModel.state.value.draft.imageObjectKey)
            assertEquals("content://image", viewModel.state.value.draft.imageUri)
        }

    @Test
    fun `upload package image failure clears selected preview`() =
        runTest {
            val s3Repository = FakePackageS3Repository(shouldFail = true)
            val viewModel = createViewModel(s3Repository = s3Repository)

            viewModel.handleIntent(MyPagePackageEditIntent.ClickAddPackage)
            viewModel.handleIntent(MyPagePackageEditIntent.ChangePackageImage("content://image"))
            viewModel.handleIntent(MyPagePackageEditIntent.UploadPackageImage(byteArrayOf(1, 2), "image.jpg"))
            advanceUntilIdle()

            assertEquals(null, viewModel.state.value.draft.imageObjectKey)
            assertEquals("", viewModel.state.value.draft.imageUri)
        }

    private fun createViewModel(
        photographerRepository: FakePackagePhotographerRepository = FakePackagePhotographerRepository(),
        s3Repository: FakePackageS3Repository = FakePackageS3Repository(),
    ): MyPagePackageEditViewModel =
        MyPagePackageEditViewModel(
            createProductUseCase = CreateProductUseCase(photographerRepository),
            getPhotographerProductsUseCase = GetPhotographerProductsUseCase(photographerRepository),
            uploadProductImageUseCase = UploadProductImageUseCase(s3Repository),
        ).apply {
            handleIntent(MyPagePackageEditIntent.LoadPhotographer(12L))
        }

    private class FakePackagePhotographerRepository(
        private val products: List<ShootingPackage> = emptyList(),
    ) : ProductRepository {
        val createdProducts = mutableListOf<CreateProductCommand>()
        var requestedPhotographerId: Long? = null

        override suspend fun getPhotographerProducts(photographerId: Long): AppResult<List<ShootingPackage>> {
            requestedPhotographerId = photographerId
            return Result.success(products)
        }

        override suspend fun createProduct(command: CreateProductCommand): AppResult<Long> {
            createdProducts += command
            return Result.success(101L)
        }
    }

    private class FakePackageS3Repository(
        private val uploadedObjectKey: String = "products/default.jpg",
        private val shouldFail: Boolean = false,
    ) : S3Repository {
        override suspend fun uploadProfileImage(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> = error("Not needed")

        override suspend fun uploadProductImage(
            imageBytes: ByteArray,
            filename: String,
        ): AppResult<String> =
            if (shouldFail) {
                Result.failure(IllegalStateException("upload failed"))
            } else {
                Result.success(uploadedObjectKey)
            }
    }
}
