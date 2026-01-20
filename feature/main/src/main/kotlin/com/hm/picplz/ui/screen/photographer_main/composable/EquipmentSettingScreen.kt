package com.hm.picplz.ui.screen.photographer_main.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainIntent
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainSideEffect
import com.hm.picplz.ui.screen.photographer_main.PhotographerMainViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EquipmentSettingScreen(
    modifier : Modifier = Modifier,
    viewModel: PhotographerMainViewModel = hiltViewModel(),
    navController: NavHostController
) {
    Scaffold {
        innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            CommonTopBar(
                text = "촬영 기기 설정",
                onClickBack = {
                    viewModel.handleIntent(PhotographerMainIntent.NavigateToPrev)
                }
            )
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