package com.hm.picplz.ui.screen.photographer_main.composable

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.domain.model.Device
import com.hm.picplz.domain.model.DeviceCategory
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.common.device.DeviceSelectorBottomSheet
import com.hm.picplz.ui.screen.common.device.DeviceSelectorBox
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainIntent
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainSideEffect
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainState
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import kotlinx.coroutines.flow.collectLatest

private data class DeviceBrandOption(
    val name: String,
    val models: List<String>,
)

@Composable
fun PhotographerAddDeviceScreen(
    category: DeviceCategory,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PhotographerMainViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = { navController.popBackStack() },
) {
    val currentState = viewModel.state.collectAsState().value
    val focusManager = LocalFocusManager.current
    val phoneBrandOptions = rememberPhoneBrandOptions()
    val directInputText = stringResource(R.string.equipment_setting_direct_input)
    val topBarText =
        when (category) {
            DeviceCategory.PHONE -> stringResource(R.string.equipment_setting_add_phone_title)
            DeviceCategory.CAMERA -> stringResource(R.string.equipment_setting_add_camera_title)
        }

    LaunchedEffect(category) {
        viewModel.handleIntent(PhotographerMainIntent.ResetCurrentDevice(category))
    }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        },
                    )
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
                        viewModel.handleIntent(PhotographerMainIntent.ResetCurrentDevice(category))
                        onNavigateBack()
                    },
                )

                when (category) {
                    DeviceCategory.PHONE -> {
                        PhoneDeviceForm(
                            currentState = currentState,
                            focusManager = focusManager,
                            viewModel = viewModel,
                        )
                    }
                    DeviceCategory.CAMERA -> {
                        CameraDeviceForm(
                            currentState = currentState,
                            focusManager = focusManager,
                            viewModel = viewModel,
                        )
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
                    text = stringResource(R.string.equipment_setting_add_device_button),
                    onClick = {
                        viewModel.handleIntent(PhotographerMainIntent.AddCurrentDeviceToList(category))
                    },
                    enabled = currentState.canAddDevice(category),
                )
            }
        }
    }

    DeviceSelectorBottomSheet(
        options =
            when (category) {
                DeviceCategory.PHONE -> phoneBrandOptions.map { it.name }
                DeviceCategory.CAMERA -> currentState.availableCameraBrands.map { it.name }
            },
        directInputText = directInputText,
        onOptionSelected = { brand ->
            when (category) {
                DeviceCategory.PHONE -> {
                    viewModel.handleIntent(
                        PhotographerMainIntent.UpdateCurrentPhone(
                            Device.PhoneDevice(
                                companyName = brand,
                                modelName = null,
                            ),
                        ),
                    )
                }
                DeviceCategory.CAMERA -> {
                    val currentCamera = currentState.currentCamera
                    viewModel.handleIntent(
                        PhotographerMainIntent.UpdateCurrentCamera(
                            Device.CameraDevice(
                                id = currentCamera?.id ?: Device.CameraDevice(companyName = brand).id,
                                companyName = brand,
                                modelName = currentCamera?.modelName,
                                cameraType = currentCamera?.cameraType,
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
                        PhotographerMainIntent.SetPhoneDirectInputMode(
                            brandMode = true,
                            modelMode = true,
                        ),
                    )
                }
                DeviceCategory.CAMERA -> {
                    viewModel.handleIntent(
                        PhotographerMainIntent.SetCameraDirectInputMode(brandMode = true),
                    )
                }
            }
        },
        onDismiss = {
            viewModel.handleIntent(PhotographerMainIntent.SetBrandExpanded(false))
        },
        visible = currentState.brandExpanded,
    )

    if (category == DeviceCategory.PHONE) {
        val models =
            phoneBrandOptions.find { it.name == currentState.currentPhone?.companyName }?.models.orEmpty()
        DeviceSelectorBottomSheet(
            options = models,
            directInputText = directInputText,
            onOptionSelected = { model ->
                val currentBrand = currentState.currentPhone?.companyName.orEmpty()
                viewModel.handleIntent(
                    PhotographerMainIntent.UpdateCurrentPhone(
                        Device.PhoneDevice(
                            id = currentState.currentPhone?.id ?: Device.PhoneDevice(companyName = currentBrand).id,
                            companyName = currentBrand,
                            modelName = model,
                        ),
                    ),
                )
            },
            onDirectInput = {
                viewModel.handleIntent(PhotographerMainIntent.SetModelDirectInput(category, true))
            },
            onDismiss = {
                viewModel.handleIntent(PhotographerMainIntent.SetModelExpanded(false))
            },
            visible = currentState.modelExpanded,
        )
    }

    if (category == DeviceCategory.CAMERA) {
        DeviceSelectorBottomSheet(
            options = currentState.availableCameraTypes,
            directInputText = directInputText,
            onOptionSelected = { type ->
                val currentCamera = currentState.currentCamera
                viewModel.handleIntent(
                    PhotographerMainIntent.UpdateCurrentCamera(
                        Device.CameraDevice(
                            id = currentCamera?.id ?: Device.CameraDevice(companyName = "").id,
                            companyName = currentCamera?.companyName.orEmpty(),
                            modelName = currentCamera?.modelName,
                            cameraType = type,
                        ),
                    ),
                )
            },
            onDismiss = {
                viewModel.handleIntent(PhotographerMainIntent.SetCameraTypeExpanded(false))
            },
            visible = currentState.cameraTypeExpanded,
        )
    }

    CommonToast(
        message = stringResource(R.string.equipment_setting_duplicate_device_message),
        isVisible = currentState.showDuplicateDeviceToast,
        onDismiss = {
            viewModel.handleIntent(PhotographerMainIntent.DismissToast)
        },
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is PhotographerMainSideEffect.NavigateToPrev -> {
                    onNavigateBack()
                }
                is PhotographerMainSideEffect.Navigate -> {
                    navController.navigate(sideEffect.destination)
                }
            }
        }
    }
}

@Composable
private fun PhoneDeviceForm(
    currentState: PhotographerMainState,
    focusManager: FocusManager,
    viewModel: PhotographerMainViewModel,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp),
    ) {
        Text(stringResource(R.string.equipment_setting_brand_label), style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentPhone?.companyName,
            placeholder =
                if (currentState.phoneBrandDirectInput) {
                    stringResource(R.string.equipment_setting_brand_input_placeholder)
                } else {
                    stringResource(R.string.equipment_setting_select_placeholder)
                },
            isSelected = currentState.currentPhone?.companyName != null,
            isDirectInput = currentState.phoneBrandDirectInput,
            inputText = currentState.currentPhone?.companyName.orEmpty(),
            onTextChange = { brand ->
                viewModel.handleIntent(
                    PhotographerMainIntent.UpdateCurrentPhone(
                        Device.PhoneDevice(
                            id = currentState.currentPhone?.id ?: Device.PhoneDevice(companyName = brand).id,
                            companyName = brand,
                            modelName = currentState.currentPhone?.modelName,
                        ),
                    ),
                )
            },
            onClick = {
                if (!currentState.phoneBrandDirectInput) {
                    focusManager.clearFocus()
                    viewModel.handleIntent(PhotographerMainIntent.SetBrandExpanded(true))
                }
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(stringResource(R.string.equipment_setting_model_label), style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentPhone?.modelName,
            placeholder = phoneModelPlaceholder(currentState),
            isSelected = currentState.currentPhone?.modelName != null,
            enabled = currentState.currentPhone?.companyName != null || currentState.phoneBrandDirectInput,
            isDirectInput = currentState.phoneBrandDirectInput || currentState.phoneModelDirectInput,
            inputText = currentState.currentPhone?.modelName.orEmpty(),
            onTextChange = { model ->
                val currentBrand = currentState.currentPhone?.companyName.orEmpty()
                viewModel.handleIntent(
                    PhotographerMainIntent.UpdateCurrentPhone(
                        Device.PhoneDevice(
                            id = currentState.currentPhone?.id ?: Device.PhoneDevice(companyName = currentBrand).id,
                            companyName = currentBrand,
                            modelName = model,
                        ),
                    ),
                )
            },
            onClick = {
                if (!currentState.phoneBrandDirectInput && currentState.currentPhone?.companyName != null) {
                    focusManager.clearFocus()
                    viewModel.handleIntent(PhotographerMainIntent.SetModelExpanded(true))
                }
            },
        )
    }
}

@Composable
private fun CameraDeviceForm(
    currentState: PhotographerMainState,
    focusManager: FocusManager,
    viewModel: PhotographerMainViewModel,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp),
    ) {
        Text(stringResource(R.string.equipment_setting_brand_label), style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentCamera?.companyName?.takeIf { it.isNotEmpty() },
            placeholder =
                if (currentState.cameraBrandDirectInput) {
                    stringResource(R.string.equipment_setting_brand_input_placeholder)
                } else {
                    stringResource(R.string.equipment_setting_select_placeholder)
                },
            isSelected = !currentState.currentCamera?.companyName.isNullOrEmpty(),
            isDirectInput = currentState.cameraBrandDirectInput,
            inputText = currentState.currentCamera?.companyName.orEmpty(),
            onTextChange = { brand ->
                val currentCamera = currentState.currentCamera
                viewModel.handleIntent(
                    PhotographerMainIntent.UpdateCurrentCamera(
                        Device.CameraDevice(
                            id = currentCamera?.id ?: Device.CameraDevice(companyName = brand).id,
                            companyName = brand,
                            modelName = currentCamera?.modelName.orEmpty(),
                            cameraType = currentCamera?.cameraType.orEmpty(),
                        ),
                    ),
                )
            },
            onClick = {
                if (!currentState.cameraBrandDirectInput) {
                    focusManager.clearFocus()
                    viewModel.handleIntent(PhotographerMainIntent.SetBrandExpanded(true))
                }
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(stringResource(R.string.equipment_setting_camera_type_label), style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentCamera?.cameraType?.takeIf { it.isNotEmpty() },
            placeholder = stringResource(R.string.equipment_setting_select_placeholder),
            isSelected = !currentState.currentCamera?.cameraType.isNullOrEmpty(),
            onClick = {
                focusManager.clearFocus()
                viewModel.handleIntent(PhotographerMainIntent.SetCameraTypeExpanded(true))
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(stringResource(R.string.equipment_setting_model_label), style = pretendardTypography.titleSmall)
        Spacer(modifier = Modifier.height(10.dp))
        DeviceSelectorBox(
            text = currentState.currentCamera?.modelName?.takeIf { it.isNotEmpty() },
            placeholder = stringResource(R.string.equipment_setting_camera_model_input_placeholder),
            isSelected = !currentState.currentCamera?.modelName.isNullOrEmpty(),
            isDirectInput = true,
            inputText = currentState.currentCamera?.modelName.orEmpty(),
            onTextChange = { modelName ->
                val currentCamera = currentState.currentCamera
                viewModel.handleIntent(
                    PhotographerMainIntent.UpdateCurrentCamera(
                        Device.CameraDevice(
                            id = currentCamera?.id ?: Device.CameraDevice(companyName = "").id,
                            companyName = currentCamera?.companyName.orEmpty(),
                            modelName = modelName,
                            cameraType = currentCamera?.cameraType.orEmpty(),
                        ),
                    ),
                )
            },
            onClick = {},
        )
    }
}

@Composable
private fun rememberPhoneBrandOptions(): List<DeviceBrandOption> {
    return listOf(
        DeviceBrandOption(
            name = stringResource(R.string.equipment_setting_phone_brand_apple),
            models = stringArrayResource(R.array.equipment_setting_apple_phone_models).toList(),
        ),
        DeviceBrandOption(
            name = stringResource(R.string.equipment_setting_phone_brand_samsung),
            models = stringArrayResource(R.array.equipment_setting_samsung_phone_models).toList(),
        ),
    )
}

@Composable
private fun phoneModelPlaceholder(currentState: PhotographerMainState): String {
    return when {
        currentState.phoneBrandDirectInput -> stringResource(R.string.equipment_setting_phone_model_input_placeholder)
        currentState.phoneModelDirectInput -> stringResource(R.string.equipment_setting_phone_model_input_placeholder)
        currentState.currentPhone?.companyName == null -> {
            stringResource(R.string.equipment_setting_model_before_brand_placeholder)
        }
        else -> stringResource(R.string.equipment_setting_select_placeholder)
    }
}

private fun PhotographerMainState.canAddDevice(category: DeviceCategory): Boolean {
    return when (category) {
        DeviceCategory.PHONE -> {
            val phone = currentPhone
            phone != null && phone.companyName.isNotEmpty() && !phone.modelName.isNullOrEmpty()
        }
        DeviceCategory.CAMERA -> {
            val camera = currentCamera
            camera != null &&
                camera.companyName.isNotEmpty() &&
                !camera.cameraType.isNullOrEmpty() &&
                !camera.modelName.isNullOrEmpty()
        }
    }
}
