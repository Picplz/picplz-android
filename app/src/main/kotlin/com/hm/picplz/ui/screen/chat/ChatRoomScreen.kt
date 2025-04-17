package com.hm.picplz.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.R
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    roomId: Int,
) {
    Scaffold(
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text = "유가영 작가",
                subText = "당장 촬영 가능",
                subTextStyle = MainFontFamily.caption.copy(color = MainThemeColor.Green120),
                onClickBack = {},
                showMenuIcon = true,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.reservation_step_one),
                    contentDescription = "레벨"
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "예약 대기",
                        style = MainFontFamily.caption,
                    )
                    Text(
                        text = "서비스 진행",
                        style = MainFontFamily.caption
                    )
                    Text(
                        text = "거래 확정",
                        style = MainFontFamily.caption
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRoomScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        ChatRoomScreen(
            navController = navController,
            roomId = 1
        )
    }
}