package com.hm.picplz.ui.screen.photographer_main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.domain.model.Equipment
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainIntent
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainSideEffect
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.core.ui.R as CoreUiR

@Composable
fun EquipmentSettingScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotographerMainViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val currentState = viewModel.state.collectAsState().value
    val enabledEquipment = currentState.equipmentList.filter { it.isEnabled }
    val phoneEquipment = enabledEquipment.filter { it.type.contains("폰") || it.type.contains("핸드폰") }
    val cameraEquipment = enabledEquipment - phoneEquipment.toSet()

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
                    viewModel.handleIntent(PhotographerMainIntent.NavigateToPrev)
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
                        EquipmentSection(
                            title = stringResource(R.string.equipment_setting_phone_section),
                            equipment = phoneEquipment,
                            onRemoveEquipment = { equipment ->
                                viewModel.handleIntent(
                                    PhotographerMainIntent.ToggleEquipmentEnabled(equipment.id),
                                )
                            },
                        )
                        EquipmentSection(
                            title = stringResource(R.string.equipment_setting_camera_section),
                            equipment = cameraEquipment,
                            onRemoveEquipment = { equipment ->
                                viewModel.handleIntent(
                                    PhotographerMainIntent.ToggleEquipmentEnabled(equipment.id),
                                )
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
                    enabled = enabledEquipment.isNotEmpty(),
                    onClick = {
                        viewModel.handleIntent(PhotographerMainIntent.NavigateToPrev)
                    },
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is PhotographerMainSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
                is PhotographerMainSideEffect.Navigate -> {}
            }
        }
    }
}

@Composable
private fun EquipmentSection(
    title: String,
    equipment: List<Equipment>,
    onRemoveEquipment: (Equipment) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = pretendardTypography.titleSmall,
        )
        equipment.forEach { item ->
            EquipmentSettingItem(
                equipment = item,
                onRemove = { onRemoveEquipment(item) },
            )
        }
    }
}

@Composable
private fun EquipmentSettingItem(
    equipment: Equipment,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MainThemeColor.Gray1)
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Gray2,
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(horizontal = 15.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = equipment.type,
                style = pretendardTypography.bodyMedium,
                color = MainThemeColor.Gray4,
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = equipment.deviceName,
                style = MainFontFamily.bodyBold,
                color = MainThemeColor.Gray5,
            )
        }
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(48.dp),
        ) {
            Image(
                painter = painterResource(id = CoreUiR.drawable.close_circle),
                contentDescription =
                    stringResource(
                        R.string.equipment_setting_remove_content_description,
                        equipment.deviceName,
                    ),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
