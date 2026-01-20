package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hm.picplz.navigation.Routes
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.screen.common.CommonFixedTopBar
import com.hm.picplz.ui.screen.my_page.shootingHistoryCard.ShootingStatus
import com.hm.picplz.ui.screen.my_page.shootingHistoryCard.SwipeableShootingHistoryCard
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun MyPageShootingHistoryScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonFixedTopBar(title = "내 촬영 내역") {
                navController.popBackStack()
            }
        },
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    SwipeableShootingHistoryCard(
                        userName = "합정동 불주먹@@",
                        userProfile = R.drawable.edit_grey4,
                        status = ShootingStatus.COMPLEETED,
                        date = "5월 26일 오전 9시 30분",
                        location = "종로구 효자로 33",
                        onClickOrderSheet = { navController.navigate(Routes.MY_PAGE_ORDER_SHEET) }
                    )
                }
                item {
                    SwipeableShootingHistoryCard(
                        userName = "합정동 불주먹",
                        userProfile = R.drawable.edit_grey4,
                        status = ShootingStatus.CANCLED,
                        date = "5월 26일 오전 9시 30분",
                        location = "종로구 효자로 33",
                    )
                }
                item {
                    SwipeableShootingHistoryCard(
                        userName = "합정동 불주먹",
                        userProfile = R.drawable.edit_grey4,
                        status = ShootingStatus.CANCLED,
                        date = "5월 26일 오전 9시 30분",
                        location = "종로구 효자로 33",
                    )
                }
                item {
                    SwipeableShootingHistoryCard(
                        userName = "합정동 불주먹",
                        userProfile = R.drawable.edit_grey4,
                        status = ShootingStatus.CANCLED,
                        date = "5월 26일 오전 9시 30분",
                        location = "종로구 효자로 33",
                    )
                }
                item {
                    SwipeableShootingHistoryCard(
                        userName = "합정동 불주먹",
                        userProfile = R.drawable.edit_grey4,
                        status = ShootingStatus.CANCLED,
                        date = "5월 26일 오전 9시 30분",
                        location = "종로구 효자로 33",
                    )
                }
            }
        }
    }
}