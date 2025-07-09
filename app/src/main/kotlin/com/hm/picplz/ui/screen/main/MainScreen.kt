package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.layout.Column
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
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var visible by remember { mutableStateOf(true) } // <-- 상태 선언

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


            RegionModalBottomSheet(
                onDismiss = { visible = false },
                visible = visible,
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
