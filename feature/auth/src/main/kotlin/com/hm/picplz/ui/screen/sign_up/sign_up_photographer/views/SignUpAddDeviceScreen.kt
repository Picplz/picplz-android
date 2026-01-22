package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.data.model.DeviceBrand
import com.hm.picplz.data.model.DeviceData
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.AddCurrentDeviceToList
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.ResetCurrentDevice
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetBrandExpanded
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetCameraDirectInputMode
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetCameraTypeExpanded
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetModelDirectInput
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetModelExpanded
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetPhoneDirectInputMode
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateCurrentCamera
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.UpdateCurrentPhone
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerState
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable.DeviceSelectorBottomSheet
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.composable.DeviceSelectorBox
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@Composable
fun SignUpAddDeviceScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = hiltViewModel(),
    signUpPhotographerNavController: NavController,
    category: DeviceCategory,
) {
    val currentState = viewModel.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    val topBarText =
        when (category) {
            DeviceCategory.PHONE -> "핸드폰 추가"
            DeviceCategory.CAMERA -> "카메라 추가"
        }

    LaunchedEffect(category) {
        viewModel.handleIntent(ResetCurrentDevice(category))
    }

    val brands: List<DeviceBrand> =
        when (category) {
            DeviceCategory.PHONE -> DeviceData.phoneDevices
            DeviceCategory.CAMERA -> DeviceData.cameraBrands.map { DeviceBrand(it, emptyList()) }
        }

    val models: List<String> =
        when (category) {
            DeviceCategory.PHONE -> {
                currentState.currentPhone?.companyName?.let { brand ->
                    DeviceData.getModelsByBrand(category, brand)
                } ?: emptyList()
            }
            DeviceCategory.CAMERA -> emptyList()
        }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                CommonTopBar(
                    text = topBarText,
                    onClickBack = {
                        viewModel.handleIntent(ResetCurrentDevice(category))
                        viewModel.handleIntent(NavigateToPrev)
                    },
                )

                when (category) {
                    DeviceCategory.PHONE -> {
                        PhoneDeviceForm(currentState, viewModel, focusManager)
                    }
                    DeviceCategory.CAMERA -> {
                        CameraDeviceForm(currentState, viewModel, focusManager)
                    }
                }
            }

            Box(
                modifier =
                    Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                CommonBottomButton(
                    text = "추가하기",
                    onClick = {
                        viewModel.handleIntent(AddCurrentDeviceToList(category))
                        viewModel.handleIntent(NavigateToPrev)
                    },
                    enabled =
                        when (category) {
                            DeviceCategory.PHONE -> {
                                val phone = currentState.currentPhone
                                phone != null &&
                                    phone.companyName.isNotEmpty() &&
                                    !phone.modelName.isNullOrEmpty()
                            }
                            DeviceCategory.CAMERA -> {
                                val camera = currentState.currentCamera
                                camera != null &&
                                    camera.companyName.isNotEmpty() &&
                                    !camera.cameraType.isNullOrEmpty() &&
                                    !camera.modelName.isNullOrEmpty()
                            }
                        },
                )
            }
        }
    }
    DeviceSelectorBottomSheet(
        options = brands.map { it.name },
        onOptionSelected = { brand ->
            when (category) {
                DeviceCategory.PHONE -> {
                    viewModel.handleIntent(
                        UpdateCurrentPhone(
                            Device.PhoneDevice(
                                id = UUID.randomUUID().toString(),
                                companyName = brand,
                                modelName = null,
                            ),
                        ),
                    )
                }
                DeviceCategory.CAMERA -> {
                    viewModel.handleIntent(
                        UpdateCurrentCamera(
                            Device.CameraDevice(
                                id = currentState.currentCamera?.id ?: UUID.randomUUID().toString(),
                                companyName = brand,
                                modelName = currentState.currentCamera?.modelName,
                                cameraType = currentState.currentCamera?.cameraType,
                            ),
                        ),
                    )
                }
            }
        },
        onDirectInput = {
            when (category) {
                DeviceCategory.PHONE -> {
                    viewModel.handleIntent(
                        SetPhoneDirectInputMode(
                            brandMode = true,
                            modelMode = true,
                        ),
                    )
                }
                DeviceCategory.CAMERA -> {
                    viewModel.handleIntent(
                        SetCameraDirectInputMode(
                            brandMode = true,
                        ),
                    )
                }
            }
        },
        onDismiss = {
            viewModel.handleIntent(SetBrandExpanded(false))
        },
        visible = currentState.brandExpanded,
    )
    if (category == DeviceCategory.CAMERA) {
        DeviceSelectorBottomSheet(
            options = DeviceData.cameraTypes,
            onOptionSelected = { type ->
                val currentBrand = currentState.currentCamera?.companyName ?: ""
                val currentModel = currentState.currentCamera?.modelName
                viewModel.handleIntent(
                    UpdateCurrentCamera(
                        Device.CameraDevice(
                            id = currentState.currentCamera?.id ?: UUID.randomUUID().toString(),
                            companyName = currentBrand,
                            modelName = currentModel,
                            cameraType = type,
                        ),
                    ),
                )
            },
            onDirectInput = null,
            onDismiss = {
                viewModel.handleIntent(SetCameraTypeExpanded(false))
            },
            visible = currentState.cameraTypeExpanded,
        )
    }
    DeviceSelectorBottomSheet(
        options = models,
        onOptionSelected = { model ->
            when (category) {
                DeviceCategory.PHONE -> {
                    val currentBrand = currentState.currentPhone?.companyName ?: ""
                    viewModel.handleIntent(
                        UpdateCurrentPhone(
                            Device.PhoneDevice(
                                id = currentState.currentPhone?.id ?: UUID.randomUUID().toString(),
                                companyName = currentBrand,
                                modelName = model,
                            ),
                        ),
                    )
                }
                DeviceCategory.CAMERA -> {
                    val currentBrand = currentState.currentCamera?.companyName ?: ""
                    val currentType = currentState.currentCamera?.cameraType ?: ""
                    viewModel.handleIntent(
                        UpdateCurrentCamera(
                            Device.CameraDevice(
                                id = currentState.currentCamera?.id ?: UUID.randomUUID().toString(),
                                companyName = currentBrand,
                                modelName = model,
                                cameraType = currentType,
                            ),
                        ),
                    )
                }
            }
        },
        onDirectInput = {
            viewModel.handleIntent(SetModelDirectInput(category, true))
        },
        onDismiss = {
            viewModel.handleIntent(SetModelExpanded(false))
        },
        visible = currentState.modelExpanded,
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    signUpPhotographerNavController.popBackStack()
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun PhoneDeviceForm(
    currentState: SignUpPhotographerState,
    viewModel: SignUpPhotographerViewModel,
    focusManager: FocusManager,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp),
    ) {
        Text("브랜드", style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentPhone?.companyName,
            placeholder = if (currentState.phoneBrandDirectInput) "브랜드명을 입력하세요" else "선택",
            isSelected = currentState.currentPhone?.companyName != null,
            isDirectInput = currentState.phoneBrandDirectInput,
            inputText = currentState.currentPhone?.companyName ?: "",
            onTextChange = { brand ->
                viewModel.handleIntent(
                    UpdateCurrentPhone(
                        Device.PhoneDevice(
                            id = currentState.currentPhone?.id ?: UUID.randomUUID().toString(),
                            companyName = brand,
                            modelName = currentState.currentPhone?.modelName,
                        ),
                    ),
                )
            },
            onClick = {
                if (!currentState.phoneBrandDirectInput) {
                    focusManager.clearFocus()
                    viewModel.handleIntent(SetBrandExpanded(true))
                }
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text("모델명", style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentPhone?.modelName,
            placeholder =
                when {
                    currentState.phoneBrandDirectInput -> "상세 모델명을 입력해 주세요 (ex, 아이폰 15 pro)"
                    currentState.phoneModelDirectInput -> "상세 모델명을 입력해 주세요 (ex, 아이폰 15 pro)"
                    currentState.currentPhone?.companyName == null -> "먼저 브랜드를 선택해주세요"
                    else -> "선택"
                },
            isSelected = currentState.currentPhone?.modelName != null,
            enabled = currentState.currentPhone?.companyName != null || currentState.phoneBrandDirectInput,
            isDirectInput = currentState.phoneBrandDirectInput || currentState.phoneModelDirectInput,
            inputText = currentState.currentPhone?.modelName ?: "",
            onTextChange = { model ->
                val currentBrand = currentState.currentPhone?.companyName ?: ""
                viewModel.handleIntent(
                    UpdateCurrentPhone(
                        Device.PhoneDevice(
                            id = currentState.currentPhone?.id ?: UUID.randomUUID().toString(),
                            companyName = currentBrand,
                            modelName = model,
                        ),
                    ),
                )
            },
            onClick = {
                if (!currentState.phoneBrandDirectInput && currentState.currentPhone?.companyName != null) {
                    focusManager.clearFocus()
                    viewModel.handleIntent(SetModelExpanded(true))
                }
            },
        )
    }
}

@Composable
private fun CameraDeviceForm(
    currentState: SignUpPhotographerState,
    viewModel: SignUpPhotographerViewModel,
    focusManager: FocusManager,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp),
    ) {
        Text("브랜드", style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentCamera?.companyName?.takeIf { it.isNotEmpty() },
            placeholder = if (currentState.cameraBrandDirectInput) "브랜드명을 입력하세요" else "선택",
            isSelected = !currentState.currentCamera?.companyName.isNullOrEmpty(),
            isDirectInput = currentState.cameraBrandDirectInput,
            inputText = currentState.currentCamera?.companyName ?: "",
            onTextChange = { brand ->
                val currentCamera = currentState.currentCamera
                viewModel.handleIntent(
                    UpdateCurrentCamera(
                        Device.CameraDevice(
                            id = currentCamera?.id ?: UUID.randomUUID().toString(),
                            companyName = brand,
                            modelName = currentCamera?.modelName ?: "",
                            cameraType = currentCamera?.cameraType ?: "",
                        ),
                    ),
                )
            },
            onClick = {
                if (!currentState.cameraBrandDirectInput) {
                    focusManager.clearFocus()
                    viewModel.handleIntent(SetBrandExpanded(true))
                }
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text("카메라 종류", style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentCamera?.cameraType?.takeIf { it.isNotEmpty() },
            placeholder = "선택",
            isSelected = !currentState.currentCamera?.cameraType.isNullOrEmpty(),
            enabled = true,
            isDirectInput = false,
            inputText = "",
            onTextChange = { },
            onClick = {
                focusManager.clearFocus()
                viewModel.handleIntent(SetCameraTypeExpanded(true))
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text("모델명", style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentCamera?.modelName?.takeIf { it.isNotEmpty() },
            placeholder = "상세 모델명을 입력해 주세요 (ex, A0000)",
            isSelected = !currentState.currentCamera?.modelName.isNullOrEmpty(),
            isDirectInput = true,
            inputText = currentState.currentCamera?.modelName ?: "",
            onTextChange = { modelName ->
                val currentCamera = currentState.currentCamera
                if (currentCamera != null) {
                    viewModel.handleIntent(
                        UpdateCurrentCamera(
                            currentCamera.copy(modelName = modelName),
                        ),
                    )
                } else {
                    if (modelName.isNotEmpty()) {
                        viewModel.handleIntent(
                            UpdateCurrentCamera(
                                Device.CameraDevice(
                                    id = UUID.randomUUID().toString(),
                                    companyName = "",
                                    modelName = modelName,
                                    cameraType = "",
                                ),
                            ),
                        )
                    }
                }
            },
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpAddDeviceScreenPreview() {
    PicplzTheme {
        val signUpPhotographerNavController = rememberNavController()

        SignUpAddDeviceScreen(
            signUpPhotographerNavController = signUpPhotographerNavController,
            category = DeviceCategory.CAMERA,
        )
    }
}
