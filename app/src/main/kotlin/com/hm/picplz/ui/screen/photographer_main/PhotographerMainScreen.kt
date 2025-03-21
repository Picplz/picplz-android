package com.hm.picplz.ui.screen.photographer_main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.model.Equipment
import com.hm.picplz.ui.screen.common.AddressMarker
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonToggleSwitch
import com.hm.picplz.ui.screen.common.RefetchButton
import com.hm.picplz.ui.screen.photographer_main.composable.EquipmentListItem
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PhotographerMainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val equipmentList = listOf(
        Equipment("내 폰", "아이폰 16 Pro Max", true),
        Equipment("카메라", "소니 a7m4", true),
        Equipment("카메라", "소니 a7c", false),
        Equipment("카메라", "소니 ZV-1", false)
    )

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
                    .background(color = MainThemeColor.Gray1)
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
                        painter = painterResource(R.drawable.double_circle),
                        contentDescription = "범위 이미지",
                        modifier = Modifier
                    )
                    Image(
                        painter = painterResource(R.drawable.searching_photographer),
                        contentDescription = "작가 캐릭터",
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(
                        vertical = 5.dp,
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
                    Text(
                        text = "장비 편집",
                        style = typography.bodySmall,
                        color = MainThemeColor.Gray4
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(equipmentList) { equipment ->
                        EquipmentListItem(
                            equipmentType = equipment.type,
                            deviceName = equipment.deviceName,
                            isEnabled = equipment.isEnabled,
                            onEnabledChanged = {}
                        )
                    }
                }
                CommonBottomButton(text = "바로 촬영 시작", onClick = { /*TODO*/ })
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