package com.hm.picplz.ui.screen.photographer_main

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.CameraBrand
import com.hm.picplz.domain.model.CameraCatalog
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.domain.repository.CameraRepository
import com.hm.picplz.domain.usecase.GetCameraCatalogUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotographerMainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads backend backed camera catalog into state`() =
        runTest {
            val viewModel = createViewModel()

            advanceUntilIdle()

            assertEquals(listOf(CameraBrand("소니", listOf("a7m4"))), viewModel.state.value.availableCameraBrands)
            assertEquals(listOf("미러리스 카메라"), viewModel.state.value.availableCameraTypes)
            assertFalse(viewModel.state.value.isCameraCatalogLoading)
            assertNull(viewModel.state.value.cameraCatalogLoadError)
        }

    @Test
    fun `adds phone and camera equipment and enables them`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()
            val phone = Device.PhoneDevice(id = "phone-new", companyName = "애플", modelName = "아이폰 15 Pro")
            val camera =
                Device.CameraDevice(
                    id = "camera-new",
                    companyName = "소니",
                    modelName = "a7c2",
                    cameraType = "미러리스 카메라",
                )

            viewModel.handleIntent(PhotographerMainIntent.UpdateCurrentPhone(phone))
            viewModel.handleIntent(PhotographerMainIntent.AddCurrentDeviceToList(DeviceCategory.PHONE))
            viewModel.handleIntent(PhotographerMainIntent.UpdateCurrentCamera(camera))
            viewModel.handleIntent(PhotographerMainIntent.AddCurrentDeviceToList(DeviceCategory.CAMERA))
            advanceUntilIdle()

            assertTrue(viewModel.state.value.phoneDevices.contains(phone))
            assertTrue(viewModel.state.value.cameraDevices.contains(camera))
            assertTrue(phone.id in viewModel.state.value.enabledEquipmentIds)
            assertTrue(camera.id in viewModel.state.value.enabledEquipmentIds)
        }

    @Test
    fun `rejects duplicate equipment`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()
            val duplicate = Device.PhoneDevice(id = "phone-duplicate", companyName = "애플", modelName = "아이폰 16 Pro Max")
            val initialPhoneCount = viewModel.state.value.phoneDevices.size

            viewModel.handleIntent(PhotographerMainIntent.UpdateCurrentPhone(duplicate))
            viewModel.handleIntent(PhotographerMainIntent.AddCurrentDeviceToList(DeviceCategory.PHONE))
            advanceUntilIdle()

            assertEquals(initialPhoneCount, viewModel.state.value.phoneDevices.size)
            assertTrue(viewModel.state.value.showDuplicateDeviceToast)
            assertFalse(duplicate.id in viewModel.state.value.enabledEquipmentIds)
        }

    @Test
    fun `removes equipment and enabled state`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()
            val phone = Device.PhoneDevice(id = "phone-remove", companyName = "삼성", modelName = "갤럭시 S24")

            viewModel.handleIntent(PhotographerMainIntent.UpdateCurrentPhone(phone))
            viewModel.handleIntent(PhotographerMainIntent.AddCurrentDeviceToList(DeviceCategory.PHONE))
            viewModel.handleIntent(PhotographerMainIntent.RemoveDeviceFromCategory(phone))
            advanceUntilIdle()

            assertFalse(viewModel.state.value.phoneDevices.contains(phone))
            assertFalse(phone.id in viewModel.state.value.enabledEquipmentIds)
        }

    @Test
    fun `reset current device clears add form state`() =
        runTest {
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.handleIntent(PhotographerMainIntent.SetPhoneDirectInputMode(brandMode = true, modelMode = true))
            viewModel.handleIntent(PhotographerMainIntent.SetBrandExpanded(true))
            viewModel.handleIntent(PhotographerMainIntent.SetModelExpanded(true))
            viewModel.handleIntent(PhotographerMainIntent.ResetCurrentDevice(DeviceCategory.PHONE))

            assertNull(viewModel.state.value.currentPhone)
            assertFalse(viewModel.state.value.phoneBrandDirectInput)
            assertFalse(viewModel.state.value.phoneModelDirectInput)
            assertFalse(viewModel.state.value.brandExpanded)
            assertFalse(viewModel.state.value.modelExpanded)
        }

    @Test
    fun `equipment mapping keeps category labels stable`() {
        val state =
            PhotographerMainState(
                phoneDevices = listOf(Device.PhoneDevice(id = "phone", companyName = "애플", modelName = "아이폰 15 Pro")),
                cameraDevices =
                    listOf(
                        Device.CameraDevice(
                            id = "camera",
                            companyName = "소니",
                            modelName = "a7m4",
                            cameraType = "미러리스 카메라",
                        ),
                    ),
                enabledEquipmentIds = setOf("phone", "camera"),
            )

        assertEquals("내 폰", state.equipmentList[0].type)
        assertEquals("카메라", state.equipmentList[1].type)
        assertEquals("아이폰 15 Pro", state.equipmentList[0].deviceName)
        assertEquals("a7m4 (미러리스 카메라)", state.equipmentList[1].deviceName)
    }

    private fun createViewModel(): PhotographerMainViewModel {
        return PhotographerMainViewModel(
            getCameraCatalogUseCase = GetCameraCatalogUseCase(FakeCameraRepository()),
        )
    }

    private class FakeCameraRepository : CameraRepository {
        override suspend fun getCameraCatalog(): AppResult<CameraCatalog> =
            Result.success(
                CameraCatalog(
                    brands = listOf(CameraBrand("소니", listOf("a7m4"))),
                    types = listOf("미러리스 카메라"),
                ),
            )
    }
}
