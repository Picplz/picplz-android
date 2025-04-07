package com.hm.picplz.ui.screen.photographer_main.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hm.picplz.ui.screen.common.CommonTopBar

@Composable
fun EquipmentSettingScreen(
    modifier : Modifier = Modifier,
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
                onClickBack = {}
            )
        }
    }
}