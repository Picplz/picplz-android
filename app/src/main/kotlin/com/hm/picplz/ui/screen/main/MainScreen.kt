package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.main.modalBottomSheet.DeviceModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.MoodKeywordModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortFilterModalBottomSheet
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var visible by remember { mutableStateOf(false) } // <-- 상태 선언
    var visibleDevice by remember { mutableStateOf(false) } // <-- 상태 선언
    var visibleDeviceMood by remember { mutableStateOf(false) } // <-- 상태 선언
    var visibleSortFilter by remember { mutableStateOf(false) } // <-- 상태 선언

    var selectedSortType by remember { mutableStateOf(SortType.POPULAR) }


    Scaffold(
        containerColor = MainThemeColor.White,
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Column {
            Text(
                text = "메인 화면",
                modifier = Modifier.padding(innerPadding),
            )

            Button(onClick = { navController.navigate("detail-photographer") }) {
                Text(text = "작사 상세 페이지 테스트 버튼")
            }
            Button(onClick = { navController.navigate("photographer-main") }) {
                Text(text = "작가 메인 페이지 테스트 버튼")
            }
            Button(onClick = { visible = true }) {
                Text(text = "지역 바텀시트 테스트 버튼")
            }
            Button(onClick = { visibleDevice = true }) {
                Text(text = "촬영기기 바텀시트 테스트 버튼")
            }
            Button(onClick = { visibleDeviceMood = true }) {
                Text(text = "분위기 키워드 바텀시트 테스트 버튼")
            }
            Row {
                Text(text = selectedSortType.label)
                Button(onClick = { visibleSortFilter = true }) {
                    Text(text = "정렬 바텀시트 테스트 버튼")
                }
            }


            RegionModalBottomSheet(
                onDismiss = { visible = false },
                visible = visible,
            )

            DeviceModalBottomSheet(onDismiss = { visibleDevice = false }, visible = visibleDevice)

            MoodKeywordModalBottomSheet(
                onDismiss = { visibleDeviceMood = false },
                visible = visibleDeviceMood
            )

            SortFilterModalBottomSheet(
                onDismiss = { visibleSortFilter = false },
                visible = visibleSortFilter,
                onSelect = { selectedType ->
                    selectedSortType = selectedType
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        MainScreen(navController = navController)
    }
}
