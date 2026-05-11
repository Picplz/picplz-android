package com.hm.picplz.ui.screen.photographer_main.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.domain.model.Device
import com.hm.picplz.feature.main.R
import com.hm.picplz.navigation.model.PhotographerAddDevice
import com.hm.picplz.ui.screen.common.CommonAddButton
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainIntent
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

private const val PHONE_CATEGORY_ROUTE_VALUE = "phone"
private const val CAMERA_CATEGORY_ROUTE_VALUE = "camera"

@Composable
fun EquipmentSettingScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotographerMainViewModel = hiltViewModel(),
    navController: NavHostController,
    onNavigateBack: () -> Unit = { navController.popBackStack() },
    onNavigateAddDevice: (String) -> Unit = { category ->
        navController.navigate(PhotographerAddDevice(category = category))
    },
) {
    val currentState = viewModel.state.collectAsState().value

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommonTopBar(
                text = stringResource(R.string.equipment_setting_top_bar_title),
                onClickBack = {
                    onNavigateBack()
                },
            )

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.equipment_setting_title),
                        style = pretendardTypography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(40.dp),
                    ) {
                        DeviceSection(
                            title = stringResource(R.string.equipment_setting_phone_section),
                            devices = currentState.phoneDevices,
                            onAddDevice = {
                                onNavigateAddDevice(PHONE_CATEGORY_ROUTE_VALUE)
                            },
                            onRemoveDevice = { device ->
                                viewModel.handleIntent(PhotographerMainIntent.RemoveDeviceFromCategory(device))
                            },
                        )
                        DeviceSection(
                            title = stringResource(R.string.equipment_setting_camera_section),
                            devices = currentState.cameraDevices,
                            onAddDevice = {
                                onNavigateAddDevice(CAMERA_CATEGORY_ROUTE_VALUE)
                            },
                            onRemoveDevice = { device ->
                                viewModel.handleIntent(PhotographerMainIntent.RemoveDeviceFromCategory(device))
                            },
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
                    text = stringResource(R.string.equipment_setting_save_button),
                    enabled = currentState.phoneDevices.isNotEmpty() || currentState.cameraDevices.isNotEmpty(),
                    onClick = {
                        onNavigateBack()
                    },
                )
            }
        }
    }
}

@Composable
private fun DeviceSection(
    title: String,
    devices: List<Device>,
    onAddDevice: () -> Unit,
    onRemoveDevice: (Device) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = pretendardTypography.titleSmall,
        )
        devices.forEach { device ->
            PhotographerDeviceItem(
                device = device,
                onRemove = { onRemoveDevice(device) },
            )
        }
        CommonAddButton(
            text = stringResource(R.string.equipment_setting_add_button),
            onClick = onAddDevice,
        )
    }
}
