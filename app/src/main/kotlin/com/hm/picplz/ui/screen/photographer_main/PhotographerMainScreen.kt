package com.hm.picplz.ui.screen.photographer_main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.model.Equipment
import com.hm.picplz.ui.screen.common.AddressMarker
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonButtonModal
import com.hm.picplz.ui.screen.common.RefetchButton
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentListItem
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.viewmodel.PhotographerMainViewModel

@Composable
fun PhotographerMainScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotographerMainViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        containerColor = MainThemeColor.White,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box (
                Modifier
                    .background(
                        color = if (currentState.isActive) MainThemeColor.Green100 else MainThemeColor.Gray1
                    )
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 5.dp, end = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AddressMarker(
                        address = "마포구 서교동"
                    )
                    RefetchButton(
                        onClick = { }
                    )
                }
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(370.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            if (currentState.isActive) R.drawable.double_circle_active else R.drawable.double_circle_inactive
                        ),
                        contentDescription = "범위 이미지",
                        modifier = Modifier
                    )
                    Image(
                        painter = painterResource(
                            if (currentState.isActive) R.drawable.searching_photographer_active else R.drawable.searching_photographer_inactive
                        ),
                        contentDescription = "작가 캐릭터",
                    )
                    if (currentState.isActive) {
                        Image(
                            modifier = Modifier
                                .offset(x = (-85).dp, y = (-85).dp)
                                .size(75.dp),
                            painter = painterResource(R.drawable.flash),
                            contentDescription = "플래시 이미지",
                        )
                        Image(
                            modifier = Modifier
                                .offset(x = 88.dp, y = 88.dp)
                                .size(55.dp),
                            painter = painterResource(R.drawable.flash),
                            contentDescription = "플래시 이미지",
                        )
                        Image(
                            modifier = Modifier
                                .offset(x = 120.dp, y = 42.dp)
                                .size(37.dp),
                            painter = painterResource(R.drawable.flash),
                            contentDescription = "플래시 이미지",
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(
                        vertical = 10.dp,
                        horizontal = 15.dp
                    )
                    .fillMaxHeight()
                ,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "촬영 가능 장비",
                        style = typography.titleSmall
                    )
                    TextButton(
                        onClick = {
                            viewModel.handleIntent(
                                PhotographerMainIntent.Navigate("photographer-equipment-setting")
                            )
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (currentState.isActive) {
                                "바로 촬영 중에는 편집할 수 없어요"
                            }else {
                                "장비 편집"
                            },
                            style = typography.bodySmall,
                            color = MainThemeColor.Gray4
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentState.equipmentList) { equipment ->
                        EquipmentListItem(
                            equipmentType = equipment.type,
                            deviceName = equipment.deviceName,
                            isEnabled = equipment.isEnabled,
                            onEnabledChanged = {
                                viewModel.handleIntent(PhotographerMainIntent.ToggleEquipmentEnabled(
                                    equipment.id
                                ))
                            },
                            isPhotographerActive = currentState.isActive,
                        )
                    }
                }
                CommonBottomButton(
                    text = if (currentState.isActive) "바로 촬영 끄기 " else "바로 촬영 시작",
                    onClick = {
                        if (currentState.isActive) {
                            viewModel.handleIntent(PhotographerMainIntent.SetIsActive(false))
                        } else {
                            viewModel.handleIntent(PhotographerMainIntent.SetIsModalOpen(true))
                        }

                    },
                    containerColor = if (currentState.isActive) MainThemeColor.Green120 else MainThemeColor.Black,
                )

                if (currentState.isModalOpen) {
                    CommonButtonModal(
                        onDismissRequest = {
                            viewModel.handleIntent(PhotographerMainIntent.SetIsModalOpen(false))
                       },
                        confirmText = "확인",
                        cancelText = "취소",
                        onConfirm = {
                            viewModel.handleIntent(PhotographerMainIntent.SetIsActive(true))
                            viewModel.handleIntent(PhotographerMainIntent.SetIsModalOpen(false))
                        },
                        onCancel = {
                            viewModel.handleIntent(PhotographerMainIntent.SetIsModalOpen(false))
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.marker_map),
                                    contentDescription = "마커 이미지",
                                    modifier = Modifier
                                        .width(49.dp)
                                        .height(62.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "2km 이내 고객에게\n내 위치 정보가 나타납니다",
                                    textAlign = TextAlign.Center,
                                    style = pretendardTypography.titleSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is PhotographerMainSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
                is PhotographerMainSideEffect.Navigate -> {
                    navController.navigate(sideEffect.destination)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerMainScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        PhotographerMainScreen(navController = navController)
    }
}