package com.hm.picplz.ui.screen.dev

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.navigation.model.Chat
import com.hm.picplz.navigation.model.ChatRoom
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.DetailPhotographerPhotoPortfolios
import com.hm.picplz.navigation.model.DetailPhotographerPhotoReviews
import com.hm.picplz.navigation.model.Feed
import com.hm.picplz.navigation.model.Login
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.navigation.model.MyPage
import com.hm.picplz.navigation.model.MyPageModifyProfile
import com.hm.picplz.navigation.model.MyPageOrderSheet
import com.hm.picplz.navigation.model.MyPageShootingHistory
import com.hm.picplz.navigation.model.PhotographerEquipmentSetting
import com.hm.picplz.navigation.model.PhotographerMain
import com.hm.picplz.navigation.model.Reservation
import com.hm.picplz.navigation.model.ReviewPhotographer
import com.hm.picplz.navigation.model.SearchPhotographer
import com.hm.picplz.navigation.model.SignUpClient
import com.hm.picplz.navigation.model.SignUpCompletion
import com.hm.picplz.navigation.model.SignUpIntro
import com.hm.picplz.navigation.model.SignUpPhotographer
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun DevScreen(navController: NavHostController) {
    Scaffold(
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🛠 DEV MENU",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // === Auth ===
            SectionTitle("Auth")
            DevButton("Login") { navController.navigate(Login) }
            DevButton("SignUpIntro") { navController.navigate(SignUpIntro()) }
            DevButton("SignUpClient") { navController.navigate(SignUpClient(userInfo = emptyUserData)) }
            DevButton("SignUpPhotographer") { navController.navigate(SignUpPhotographer(userInfo = emptyUserData)) }
            DevButton("SignUpCompletion") { navController.navigate(SignUpCompletion(userInfo = emptyUserData)) }

            // === Main Tabs ===
            SectionTitle("Main Tabs")
            DevButton("Main (홈)") { navController.navigate(Main) }
            DevButton("Feed (피드)") { navController.navigate(Feed) }
            DevButton("Reservation (예약)") { navController.navigate(Reservation) }
            DevButton("Chat (채팅)") { navController.navigate(Chat) }
            DevButton("MyPage (마이페이지)") { navController.navigate(MyPage) }

            // === Main Sub Screens ===
            SectionTitle("Main Sub")
            DevButton("MainSearch") { navController.navigate(MainSearch) }
            DevButton("MyPageModifyProfile") { navController.navigate(MyPageModifyProfile) }
            DevButton("MyPageShootingHistory") { navController.navigate(MyPageShootingHistory) }
            DevButton("MyPageOrderSheet") { navController.navigate(MyPageOrderSheet) }

            // === Photographer ===
            SectionTitle("Photographer")
            DevButton("SearchPhotographer (지도)") { navController.navigate(SearchPhotographer) }
            DevButton("PhotographerMain (작가홈)") { navController.navigate(PhotographerMain) }
            DevButton("PhotographerEquipmentSetting") {
                navController.navigate(
                    PhotographerEquipmentSetting,
                )
            }

            // === Detail Photographer ===
            SectionTitle("Detail Photographer")
            DevButton("DetailPhotographer") { navController.navigate(DetailPhotographer) }
            DevButton("ReviewPhotographer") { navController.navigate(ReviewPhotographer) }
            DevButton("DetailPhotographerPhotoReviews") {
                navController.navigate(
                    DetailPhotographerPhotoReviews,
                )
            }
            DevButton("DetailPhotographerPhotoPortfolios") {
                navController.navigate(
                    DetailPhotographerPhotoPortfolios,
                )
            }

            // === Chat ===
            SectionTitle("Chat")
            DevButton("ChatRoom (test-room)") { navController.navigate(ChatRoom(roomId = "test-room-123")) }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MainThemeColor.Gray3)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MainThemeColor.Gray5,
        )
    }
}

@Composable
private fun DevButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.Black,
                contentColor = Color.White,
            ),
    ) {
        Text(text = label)
    }
}
